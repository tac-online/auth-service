package de.johanneswirth.tac.authservice.services;

import de.johanneswirth.tac.authservice.AuthDAO;
import de.johanneswirth.tac.authservice.PasswordUtils;
import de.johanneswirth.tac.common.Status;
import org.jdbi.v3.core.Jdbi;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static de.johanneswirth.tac.common.ServiceUtils.getResponse;

@Path("auth/request-pass")
public class PasswordRecoverService {

    private final String METHODS = "POST";

    private AuthDAO dao;
    private PasswordUtils utils;

    public PasswordRecoverService(Jdbi jdbi, PasswordUtils utils) {
        this.dao = jdbi.onDemand(AuthDAO.class);
        this.utils = utils;
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response resetPass(ResetRequest request) {
        return getResponse(Status.OK(), METHODS);
    }

    private class ResetRequest {
        public String email;
    }

    @OPTIONS
    public Response options() {
        return getResponse(null, METHODS);
    }
}
