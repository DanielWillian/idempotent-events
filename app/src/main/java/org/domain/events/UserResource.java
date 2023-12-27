package org.domain.events;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.domain.events.domain.UserService;
import org.domain.events.domain.User;

@Path("/users")
public class UserResource {
    @Inject
    private UserService userService;

    @POST
    @Path("/best-effort")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UserPostResponse addUserBestEffort(UserPostRequest request) {
        User user = userService.createUserBestEffort(request.getName());
        return UserPostResponse.fromDomain(user);
    }
}
