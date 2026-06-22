package pe.upc.pescagobackend.iam.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.upc.pescagobackend.iam.application.internal.queryservices.UserProfileQueryService;
import pe.upc.pescagobackend.iam.interfaces.rest.resources.SessionProfileResource;
import pe.upc.pescagobackend.shared.infrastructure.security.AuthenticatedUser;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v2/users", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Users v2", description = "Authenticated user profile endpoints for PescaGo v2")
public class ProfileController {

    private final UserProfileQueryService userProfileQueryService;

    public ProfileController(UserProfileQueryService userProfileQueryService) {
        this.userProfileQueryService = userProfileQueryService;
    }

    @GetMapping("/me/profile")
    @Operation(summary = "Current session profile", description = "Returns the authenticated user and linked business profile")
    public ResponseEntity<SessionProfileResource> getCurrentProfile(@AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        if (authenticatedUser == null) {
            return ResponseEntity.status(401).build();
        }

        return userProfileQueryService.getSessionProfile(authenticatedUser.getUserId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
