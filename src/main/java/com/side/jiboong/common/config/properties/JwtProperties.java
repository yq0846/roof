package com.side.jiboong.common.config.properties;

import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Duration;
import java.util.Base64;

@ConfigurationProperties(prefix = "spring.security.jwt")
@Slf4j
public class JwtProperties {
    private final Boolean enabled;
    private final String subject;
    private final Duration accessTokenExpiration;
    private final Duration refreshTokenExpiration;
    private final Algorithm algorithm;

    @ConstructorBinding
    public JwtProperties(
            Boolean enabled,
            String subject,
            Duration accessTokenExpiration,
            Duration refreshTokenExpiration,
            String publicKey,
            String privateKey
    ) {
        this.enabled = enabled;
        this.subject = subject;
        this.accessTokenExpiration = isInvalid(accessTokenExpiration)
                ? Duration.ofMinutes(5L)
                : accessTokenExpiration;
        this.refreshTokenExpiration = isInvalid(refreshTokenExpiration)
                ? Duration.ofDays(15L)
                : refreshTokenExpiration;
        this.algorithm = getRsaAlgorithm(publicKey, privateKey);
    }

    public Boolean enabled() {
        return enabled;
    }

    public Duration accessTokenExpiration() {
        return accessTokenExpiration;
    }

    public Duration refreshTokenExpiration() {
        return refreshTokenExpiration;
    }

    public String subject() {
        return subject;
    }

    public Algorithm algorithm() {
        return algorithm;
    }

    private boolean isInvalid(Duration duration) {
        return duration == null
                || duration.isNegative()
                || duration.isZero();
    }

    private Algorithm getRsaAlgorithm(String publicKey, String privateKey) {
        byte[] decodedPublicKey = Base64.getDecoder().decode(publicKey);
        byte[] decodedPrivateKey = Base64.getDecoder().decode(privateKey);

        try {
            RSAPublicKey rsaPublicKey = (RSAPublicKey) KeyFactory
                    .getInstance("RSA")
                    .generatePublic(new X509EncodedKeySpec(decodedPublicKey));

            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) KeyFactory
                    .getInstance("RSA")
                    .generatePrivate(new PKCS8EncodedKeySpec(decodedPrivateKey));

            return Algorithm.RSA256(rsaPublicKey, rsaPrivateKey);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error("Error creating RSA Algorithm", e);
            throw new InternalError(e);
        }
    }
}
