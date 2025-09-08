package kg.mlsp.staffcontrol.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtTokenPair {
    private String accessToken;
    private String refreshToken;
}

