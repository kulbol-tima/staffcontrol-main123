package kg.mlsp.staffcontrol.controller;

import kg.mlsp.staffcontrol.dto.AuthRequestDto;
import kg.mlsp.staffcontrol.dto.ChangePasswordDto;
import kg.mlsp.staffcontrol.dto.JwtTokenPair;
import kg.mlsp.staffcontrol.dto.request.AccessTokenRequestDto;
import kg.mlsp.staffcontrol.dto.request.TokenRefreshRequestDto;

import kg.mlsp.staffcontrol.exception.TokenIsNotValidException;
import kg.mlsp.staffcontrol.model.User;
import kg.mlsp.staffcontrol.repository.UserRepository;
import kg.mlsp.staffcontrol.service.UserService;
import kg.mlsp.staffcontrol.util.AccessPolicy;
import kg.mlsp.staffcontrol.util.JwtUtil;
import kg.mlsp.staffcontrol.util.UserPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/staffcontrol/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDto authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );

        User user = userRepository.findByUsername(authRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(authRequest.getUsername()));

        if (!user.getIsActive()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not active");
        }
        JwtTokenPair tokens = jwtUtil.generateTokens(user);
        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody TokenRefreshRequestDto dto) {
        try {
            String username = jwtUtil.extractUsername(dto.getRefreshToken());

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException(username));

            if (!user.getIsActive()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not active");
            }
            if (!jwtUtil.isTokenExpired(dto.getRefreshToken())) {
                JwtTokenPair tokens = jwtUtil.generateTokens(user);
                return ResponseEntity.ok(tokens);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token expired");
            }

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }
    }

    @AccessPolicy(
            roles = {"SUPER_ADMIN", "ADMIN", "USER"}
    )
    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDto dto) {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();

        userService.changePassword(principal.getUsername(), dto.getOldPassword(), dto.getNewPassword());
        return ResponseEntity.ok("Пароль успешно изменён");
    }

//    check token is valid
    @PostMapping("/check-token")
    public ResponseEntity<?> checkToken(@RequestBody AccessTokenRequestDto accessTokenRequestDto) {
        String token = accessTokenRequestDto.getAccessToken();
//        check if token is valid
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token is required");
        }
        if (!jwtUtil.isTokenValid(token)) {
            throw new TokenIsNotValidException("JWT token is not valid");
        }
        if (jwtUtil.isTokenExpired(token)) {
            throw new TokenIsNotValidException("JWT token is expired");
        }
        String username = jwtUtil.extractUsername(token);


        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new TokenIsNotValidException("User not found for the provided token");
        }

        if (!user.get().getIsActive()) {
            throw new TokenIsNotValidException("User is not active");
        }
        return ResponseEntity.ok("Token is valid");
    }
}

