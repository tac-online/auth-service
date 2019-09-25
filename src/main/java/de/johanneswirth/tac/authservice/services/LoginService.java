package de.johanneswirth.tac.authservice.services;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import de.johanneswirth.tac.authservice.AuthDAO;
import de.johanneswirth.tac.authservice.PasswordUtils;
import de.johanneswirth.tac.common.IStatus;
import org.apache.commons.lang3.tuple.Pair;
import org.hibernate.validator.constraints.NotEmpty;
import org.jdbi.v3.core.Jdbi;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static de.johanneswirth.tac.common.SuccessStatus.OK;

@Path("login")
public class LoginService {

    private AuthDAO dao;
    private PasswordUtils utils;

    public LoginService(Jdbi jdbi, PasswordUtils utils) {
        this.dao = jdbi.onDemand(AuthDAO.class);
        this.utils = utils;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Valid
    @NotNull
    @Timed
    @ExceptionMetered
    public IStatus<String> login(@NotNull @Valid User user) {
        if (dao.userExists(user.username) == 0) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        } else {
            Pair<String, String> password = dao.getPasswordHash(user.username);
            if (utils.verifyUserPassword(user.password, password.getLeft(), password.getRight())) {
                return OK(utils.generateJWTToken(dao.getID(user.username)), 0);
            } else {
                throw new WebApplicationException(Response.Status.UNAUTHORIZED);
            }
        }
    }

    @OPTIONS
    public void options() {}

    public static class User {
        @NotEmpty
        public String username;
        @NotEmpty
        public String password;

        public User() { }
    }
}
