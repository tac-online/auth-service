package de.johanneswirth.tac.authservice.services;

import de.johanneswirth.tac.authservice.AuthDAO;
import de.johanneswirth.tac.authservice.Password;
import de.johanneswirth.tac.authservice.PasswordUtils;
import de.johanneswirth.tac.common.Secured;
import de.johanneswirth.tac.common.Status;
import org.jdbi.v3.core.Jdbi;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import java.util.logging.Level;

import static de.johanneswirth.tac.common.ServiceUtils.getResponse;
import static de.johanneswirth.tac.common.Utils.LOGGER;

@Path("auth/change-pass")
public class PasswordChangeService {

    private final String METHODS = "POST";

    private AuthDAO dao;
    private PasswordUtils utils;

    public PasswordChangeService(Jdbi jdbi, PasswordUtils utils) {
        this.dao = jdbi.onDemand(AuthDAO.class);
        this.utils = utils;
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @Secured
    public Response register(Password password, @Context SecurityContext securityContext) {
        if (password == null) {
            return getResponse(Status.ILLEGAL_PARAMETERS, METHODS);
        }
        try {
            String salt = utils.getSalt(30);
            String pass = utils.generateSecurePassword(password.getPassword(), salt);
            dao.setPassword(pass, salt, Integer.valueOf(securityContext.getUserPrincipal().getName()));
            return getResponse(Status.OK(), METHODS);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Uncaught Exception", e);
            return Response.serverError().build();
        }
    }

    @OPTIONS
    public Response options() {
        return getResponse(null, METHODS);
    }
}
