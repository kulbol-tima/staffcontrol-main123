package kg.mlsp.staffcontrol.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import kg.mlsp.staffcontrol.config.JwtProperties;
import kg.mlsp.staffcontrol.dto.JwtTokenPair;

import kg.mlsp.staffcontrol.model.Role;
import kg.mlsp.staffcontrol.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtUtil {

    private final JwtProperties jwtProperties;
    private final SecretKey key;
    private final JwtParser parser;

    public JwtUtil(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        // Генерируем SecretKey из секретной строки (нужно для новой версии jjwt)
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
        this.parser = Jwts.parserBuilder().setSigningKey(key).build();
    }

    public JwtTokenPair generateTokens(User user) {
        Date now = new Date();
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getUsername());
        claims.put("roles", user.getRoles().stream()
                .map(Role::getCode)
                .toList());
        claims.put("subServices", user.getSubServices() != null
                ? user.getSubServices().stream()
                        .map(subService -> subService.getCode())
                        .toList()
                : null);
        claims.put("mustChangePassword", user.isMustChangePassword());
        claims.put("position", user.getStaff() != null && user.getStaff().getPosition() != null
                ? user.getStaff().getPosition().getCode()
                : null);
        claims.put("organization", user.getStaff() != null && user.getStaff().getOrganization() != null
                ? user.getStaff().getOrganization().getCode()
                : null);

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + jwtProperties.getExpirationAccessTokenTime()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + jwtProperties.getExpirationRefreshTokenTime()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return new JwtTokenPair(accessToken, refreshToken);
    }

    public String extractUsername(String token) {
        return parser.parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        return extractUsername(token).equals(userDetails.getUsername());
    }

    public boolean isTokenValid(String token) {
        try {
            parser.parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        return parser.parseClaimsJws(token).getBody().getExpiration().before(new Date());
    }

    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        Claims claims = parser.parseClaimsJws(token).getBody();
        return (List<String>) claims.get("roles");
    }

    public String extractOrganizationCode(String token) {
        Claims claims = parser.parseClaimsJws(token).getBody();
        return claims.get("organization", String.class);
    }

    public String extractPositionCode(String token) {
        Claims claims = parser.parseClaimsJws(token).getBody();
        return claims.get("position", String.class);
    }
}
