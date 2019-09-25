package de.johanneswirth.tac.authservice.services;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import de.johanneswirth.tac.authservice.AuthDAO;
import de.johanneswirth.tac.authservice.PasswordUtils;
import de.johanneswirth.tac.common.IStatus;
import org.jdbi.v3.core.Jdbi;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import static de.johanneswirth.tac.common.SuccessStatus.OK;

@Path("request-pass")
public class PasswordRecoverService {
    private AuthDAO dao;
    private PasswordUtils utils;

    public PasswordRecoverService(Jdbi jdbi, PasswordUtils utils) {
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
    public IStatus resetPass(ResetRequest request) {
        return OK;
    }

    private class ResetRequest {
        public String email;
    }

    @OPTIONS
    public void options() {}
}
