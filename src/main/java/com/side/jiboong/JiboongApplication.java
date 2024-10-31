package com.side.jiboong;

import com.side.jiboong.common.config.properties.JwtProperties;
import com.side.jiboong.common.config.properties.PathProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(value = { PathProperties.class, JwtProperties.class })
public class JiboongApplication {

    public static void main(String[] args) {
        SpringApplication.run(JiboongApplication.class, args);
    }

}
