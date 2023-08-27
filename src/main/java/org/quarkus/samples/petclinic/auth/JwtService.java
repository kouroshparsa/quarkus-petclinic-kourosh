package org.quarkus.samples.petclinic.auth;

import io.smallrye.jwt.build.Jwt;

import javax.enterprise.context.ApplicationScoped;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import io.smallrye.jwt.build.JwtClaimsBuilder;

@ApplicationScoped
public class JwtService {
    public String generateJwt(String email){
        Set<String> roles = new HashSet<>(
            Arrays.asList("admin")
        );

        JwtClaimsBuilder claimsBuilder = Jwt.claims()
                .issuer("bc-service")
                .subject("bc-service")
                .groups(roles)
                .expiresIn(3600)
                .upn(email);
        return claimsBuilder.sign();
    }
}
