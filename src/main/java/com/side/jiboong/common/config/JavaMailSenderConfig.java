package com.side.jiboong.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Map;
import java.util.Properties;

@Slf4j
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({ MailProperties.class })
@ConditionalOnProperty(prefix = "spring.mail", name = "host")
class JavaMailSenderConfig {

    @Bean
    @ConditionalOnMissingBean(JavaMailSender.class)
    JavaMailSenderImpl mailSender(MailProperties properties) {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        applyProperties(properties, sender);
        log.info("""
JavaMailSenderImpl bean created with the following properties:
                host: {}
                port: {}
                username: {}
                protocol: {}
                defaultEncoding: {}
                properties: {}
                """,
                sender.getHost(),
                sender.getPort(),
                sender.getUsername(),
                sender.getProtocol(),
                sender.getDefaultEncoding(),
                sender.getJavaMailProperties()
        );
        return sender;
    }

    private void applyProperties(MailProperties properties, JavaMailSenderImpl sender) {
        sender.setHost(properties.getHost());
        if (properties.getPort() != null) {
            sender.setPort(properties.getPort());
        }
        sender.setUsername(properties.getUsername());
        sender.setPassword(properties.getPassword());
        sender.setProtocol(properties.getProtocol());
        if (properties.getDefaultEncoding() != null) {
            sender.setDefaultEncoding(properties.getDefaultEncoding().name());
        }
        if (!properties.getProperties().isEmpty()) {
            sender.setJavaMailProperties(asProperties(properties.getProperties()));
        }
    }

    private Properties asProperties(Map<String, String> source) {
        Properties properties = new Properties();
        properties.putAll(source);
        return properties;
    }
}