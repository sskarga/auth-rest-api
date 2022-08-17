package com.store.restapi.security.jwt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

//@Configuration
@ConfigurationProperties(prefix = "application.jwt")
@Getter
@Setter
@NoArgsConstructor
public class JwtConfig {
    private String issuer;
    private String prefix;
    private String tokenAccessSecretKey;
    private Duration tokenAccessExpirationAfterDuration;
    private String tokenRefreshSecretKey;
    private Duration tokenRefreshExpirationAfterDuration;

    public LocalDateTime refreshExpiresAtLocalDateTime() {
        return Instant.ofEpochMilli(
                System.currentTimeMillis() + this.getTokenRefreshExpirationAfterDuration().toMillis()
        ).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public Date accessExpiresAtDate() {
        return new Date(System.currentTimeMillis() + this.getTokenAccessExpirationAfterDuration().toMillis());
    }

    public Date refreshExpiresAtDate() {
        return new Date(System.currentTimeMillis() + this.getTokenRefreshExpirationAfterDuration().toMillis());
    }
}
