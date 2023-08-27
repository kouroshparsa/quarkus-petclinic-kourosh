package org.quarkus.samples.petclinic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.ws.rs.FormParam;

import io.quarkus.hibernate.orm.panache.PanacheEntity;


@Entity
@Table(name = "users")
public class User extends Person {

    @Column(name = "email")
    @NotEmpty
    @FormParam("email")
    public String email;

    @Column(name = "password")
    @NotEmpty
    @FormParam("password")
    public String password;

    public static User findByEmail(String email) {
        return find("email", email).firstResult();
    }
}