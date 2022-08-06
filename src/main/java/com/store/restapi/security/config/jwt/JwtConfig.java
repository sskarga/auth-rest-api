package com.store.restapi.security.config.jwt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

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
}
