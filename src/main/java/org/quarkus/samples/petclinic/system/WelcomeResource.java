package org.quarkus.samples.petclinic.system;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import io.quarkus.qute.TemplateInstance;

@Path("/")
public class WelcomeResource {
    
    @Inject
    TemplatesLocale templates;

    @GET
    @PermitAll
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance get(@Context HttpHeaders headers) {
        boolean tokenCookiePresent = headers.getCookies().containsKey("token");
        return templates.welcome(tokenCookiePresent);
    }

}
