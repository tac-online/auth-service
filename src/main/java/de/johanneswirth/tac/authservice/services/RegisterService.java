package de.johanneswirth.tac.authservice.services;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import de.johanneswirth.tac.authservice.AuthDAO;
import de.johanneswirth.tac.authservice.PasswordUtils;
import de.johanneswirth.tac.common.IStatus;
import org.hibernate.validator.constraints.NotEmpty;
import org.jdbi.v3.core.Jdbi;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import static de.johanneswirth.tac.common.SuccessStatus.OK;

@Path("register")
public class RegisterService {

    private AuthDAO dao;
    private PasswordUtils utils;

    public RegisterService(Jdbi jdbi, PasswordUtils utils) {
        this.dao = jdbi.onDemand(AuthDAO.class);
        this.utils = utils;
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @Valid
    @NotNull
    @Timed
    @ExceptionMetered
    public IStatus<String> register(@Valid @NotNull User user) {
        if (dao.userExists(user.username) != 0) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        } else {
            String salt = utils.getSalt(30);
            String password = utils.generateSecurePassword(user.password, salt);
            dao.registerUser(user.username, user.email, password, salt);
            return OK(utils.generateJWTToken(dao.getID(user.username)), 0);
        }
    }

    @OPTIONS
    public void options() {}

    private static class User {
        @NotEmpty
        public String username;
        @NotNull
        public String email;
        @NotEmpty
        public String password;

        public User() { }
    }

}
