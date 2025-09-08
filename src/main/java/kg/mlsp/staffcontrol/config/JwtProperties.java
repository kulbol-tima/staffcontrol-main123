package kg.mlsp.staffcontrol.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String secret;

    private long expirationAccessTokenTime; // in milliseconds

    private long expirationRefreshTokenTime; // in milliseconds

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getExpirationAccessTokenTime() {
        return expirationAccessTokenTime;
    }

    public void setExpirationAccessTokenTime(long expirationAccessTokenTime) {
        this.expirationAccessTokenTime = expirationAccessTokenTime;
    }

    public long getExpirationRefreshTokenTime() {
        return expirationRefreshTokenTime;
    }

    public void setExpirationRefreshTokenTime(long expirationRefreshTokenTime) {
        this.expirationRefreshTokenTime = expirationRefreshTokenTime;
    }
}
