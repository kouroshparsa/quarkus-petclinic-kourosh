package org.quarkus.samples.petclinic.auth;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("jwt")
@ApplicationScoped
public class JwtResource {
    @Inject
    JwtService jwtService;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response getJwt(String email){
        String jwt = jwtService.generateJwt(email);
        return Response.ok(jwt).build();
    }
}
