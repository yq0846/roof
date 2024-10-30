package com.side.jiboong.common.config.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@Getter
@ConfigurationProperties(prefix = "path")
public class PathProperties {

    private final WebResources webResources;

    @ConstructorBinding
    public PathProperties(WebResources webResources) {
        this.webResources = webResources;
    }

    public record WebResources(String file) {
        @ConstructorBinding public WebResources {}
    }
}
