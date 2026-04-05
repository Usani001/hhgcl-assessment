package com.hhgcl.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.security")
@Getter
@Setter
public class SecurityProperties {
    private String secret;
    private long expiration;
}
