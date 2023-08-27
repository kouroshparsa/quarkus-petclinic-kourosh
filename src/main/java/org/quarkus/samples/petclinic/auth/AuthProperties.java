package org.quarkus.samples.petclinic.auth;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "auth")
public class AuthProperties {
    private int maxCookieAge = 3600;
}
