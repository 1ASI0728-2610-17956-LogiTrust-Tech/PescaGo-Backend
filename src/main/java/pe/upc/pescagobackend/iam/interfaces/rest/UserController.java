package pe.upc.pescagobackend.iam.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.upc.pescagobackend.iam.domain.model.aggregates.User;
import pe.upc.pescagobackend.iam.domain.model.commands.DeleteUserCommand;
import pe.upc.pescagobackend.iam.domain.model.queries.GetUserByAuthenticationQuery;
import pe.upc.pescagobackend.iam.domain.model.queries.GetUserByIdQuery;
import pe.upc.pescagobackend.iam.domain.services.UserCommandService;
import pe.upc.pescagobackend.iam.domain.services.UserQueryService;
import pe.upc.pescagobackend.iam.interfaces.rest.resources.CreateUserResource;
import pe.upc.pescagobackend.iam.interfaces.rest.resources.UserResource;
import pe.upc.pescagobackend.iam.interfaces.rest.transform.CreateUserCommandFromResourceAssembler;
import pe.upc.pescagobackend.iam.interfaces.rest.transform.UserResourceFromEntityAssembler;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/users", produces = APPLICATION_JSON_VALUE)
@Tag(name = "User", description = "Available User Endpoints")
public class UserController {
    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    public UserController(UserCommandService userCommandService, UserQueryService userQueryService) {
        this.userCommandService = userCommandService;
        this.userQueryService = userQueryService;
    }

    @PostMapping
    @Operation(summary = "Create a User", description = "Create a User")
    public ResponseEntity<User> createUser(@RequestBody CreateUserResource resource) {
        try {
            var createUserCommand = CreateUserCommandFromResourceAssembler.toCommandFromResource(resource);
            var createdUser = userCommandService.handle(createUserCommand)
                    .orElseThrow(() -> new IllegalArgumentException("Error creating user"));
            return ResponseEntity.status(201).body(createdUser);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(400).body(null);
        }
    }

    @DeleteMapping("{userId}")
    @Operation(summary = "Delete a User", description = "Delete a User by id")
    public void deleteUser(@PathVariable Long userId) {
        var deleteUserCommand = new DeleteUserCommand(userId);
        userCommandService.handle(deleteUserCommand);
    }

    @GetMapping("{userId}")
    @Operation(summary = "Get User by id", description = "Get User by id")
    public ResponseEntity<UserResource> getUser(@PathVariable Long userId) {
        var query = new GetUserByIdQuery(userId);
        var userOptional = userQueryService.handle(query);
        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(userOptional.get());
        return ResponseEntity.ok(userResource);
    }

    @GetMapping("/authentication")
    @Operation(summary = "Get User by Authentication", description = "Get User by email and password")
    public ResponseEntity<UserResource> getUserByAuthentication(
            @RequestParam String email,
            @RequestParam String password) {
        var query = new GetUserByAuthenticationQuery(email, password);
        var userOptional = userQueryService.getUserByAuthentication(query);
        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(userOptional.get());
        return ResponseEntity.ok(userResource);
    }

}
