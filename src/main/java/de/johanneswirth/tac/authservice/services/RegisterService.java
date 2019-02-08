package de.johanneswirth.tac.authservice.services;

import de.johanneswirth.tac.authservice.AuthDAO;
import de.johanneswirth.tac.authservice.PasswordUtils;
import de.johanneswirth.tac.authservice.User;
import de.johanneswirth.tac.common.Status;
import org.jdbi.v3.core.Jdbi;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Level;

import static de.johanneswirth.tac.common.ServiceUtils.getResponse;
import static de.johanneswirth.tac.common.Utils.LOGGER;

@Path("auth/register")
public class RegisterService {

    private final String METHODS = "POST";

    private AuthDAO dao;
    private PasswordUtils utils;

    public RegisterService(Jdbi jdbi, PasswordUtils utils) {
        this.dao = jdbi.onDemand(AuthDAO.class);
        this.utils = utils;
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response register(User user) {
        if (user == null) {
            return getResponse(Status.ILLEGAL_PARAMETERS, METHODS);
        }
        try {
            Status status;
            if (dao.userExists(user.getEmail()) != 0) {
                status = Status.USER_EXISTS;
            } else {
                String salt = utils.getSalt(30);
                String password = utils.generateSecurePassword(user.getPassword(), salt);
                dao.registerUser(user.getEmail(), password, salt);
                status = Status.OK(utils.generateJWTToken(dao.getID(user.getEmail())));
            }
            return getResponse(status, METHODS);
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
