package org.quarkus.samples.petclinic.auth;

import io.quarkus.qute.TemplateInstance;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import org.quarkus.samples.petclinic.model.User;
import org.quarkus.samples.petclinic.system.TemplatesLocale;
import io.quarkus.elytron.security.common.BcryptUtil;

@Path("/login")
public class LoginResource {
    @Inject
    AuthProperties authProperties;

    @Inject
    TemplatesLocale templates;

    @Inject
    JwtService jwtService;

    @GET
    @PermitAll
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance get(@QueryParam("error") String action) {
        return templates.loginForm(action);
    }

    @POST
    @PermitAll
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response login(@FormParam("email") String email, @FormParam("password") String password) {
        var user = authenticate(email, password);
        if(user != null) {
            String token = jwtService.generateJwt(email);
            return Response.seeOther(UriBuilder.fromUri("/").build())
                    .cookie(new NewCookie("token", token,
                            "/", null, null, authProperties.getMaxCookieAge(), false, false))
                    .build();
        }
        return Response.seeOther(UriBuilder.fromPath("/login").replaceQuery("error=1").build()).build();
    }

    public User authenticate(String email, String password) {
        User user = User.findByEmail(email);
        if (user != null) {
            if(BcryptUtil.matches(password, user.password))
                return user;
        }
        return null;
    }

}
