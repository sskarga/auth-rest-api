package com.store.restapi.security.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.store.restapi.security.domain.model.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {

    private final JwtConfig jwtConfig;

    private Algorithm getAlgorithmAccess() {
        return Algorithm.HMAC256(jwtConfig.getTokenAccessSecretKey());
    }

    private Algorithm getAlgorithmRefresh() {
        return Algorithm.HMAC512(jwtConfig.getTokenRefreshSecretKey());
    }

    // Generate token --------------------------------------------------------------------------------------------------
    public String getAccessToken(UserDetailsImpl user) {

        return getToken(getAlgorithmAccess(), user, jwtConfig.getTokenAccessExpirationAfterDuration().toMillis());
    }

    public String getRefreshToken(UserDetailsImpl user) {
        return getToken(getAlgorithmRefresh(), user, jwtConfig.getTokenRefreshExpirationAfterDuration().toMillis());
    }

    private String getToken(Algorithm algorithm, UserDetailsImpl user, Long expiresTime) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withIssuer(jwtConfig.getIssuer())
                .withJWTId(user.getUserId().toString())
                .withExpiresAt(new Date(System.currentTimeMillis() + expiresTime))
                .withClaim("role", user.getAuthorities().iterator().next().toString())
                .withClaim("username", user.getFullUserName())
                .sign(algorithm);
    }

    // Verify token ----------------------------------------------------------------------------------------------------
    public DecodedJWT verifyAccessToken(String token) {
        return verifyToken(getAlgorithmAccess(), token);
    }

    public DecodedJWT verifyRefreshToken(String token) {
        return verifyToken(getAlgorithmRefresh(), token);
    }

    private DecodedJWT verifyToken(Algorithm algorithm, String token) {
        try {
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(jwtConfig.getIssuer())
                    .build();
            return verifier.verify(token);
        } catch (JWTVerificationException exception){
            log.error("verifyToken: Invalid signature/claims");
        }
        return null;
    }
}