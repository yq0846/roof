package com.side.jiboong.common.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.time.Duration;

@ConfigurationProperties(prefix = "spring.mail.expiration")
public record ExpirationProperties(Duration passwordReset, Duration emailVerification) {

    @ConstructorBinding
    public ExpirationProperties {
        if (isInvalid(emailVerification)) {
            emailVerification = Duration.ofMinutes(5L);
        }
    }

    private boolean isInvalid(Duration duration) {
        return duration == null
                || duration.isNegative()
                || duration.isZero();
    }
}
