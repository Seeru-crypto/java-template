package grp.javatemplate.controller;

import grp.javatemplate.controller.dto.UserDto;
import grp.javatemplate.mapper.UserMapper;
import grp.javatemplate.model.User;
import grp.javatemplate.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static grp.javatemplate.model.enums.UserRole.ROLE_ADMIN;
import static grp.javatemplate.model.enums.UserRole.ROLE_REGULAR;
import static org.springframework.http.ResponseEntity.created;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping(path = "${endpoint.users}")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @Operation(summary = "Get all users, sort by defaults to id, page number defaults to 0, page size defaults to 10")
    @GetMapping
    public  ResponseEntity<Page<UserDto>> findAll(
            @RequestParam(name = "sortBy",required = false) String sortBy,
            @RequestParam(name = "pageNumber",required = false) Integer pageNumber,
            @RequestParam(name = "pageSize",required = false) Integer pageSize
    ) {
        log.info("REST request to findAll users");
        Page<User> res = userService.findAllPages(sortBy, pageNumber, pageSize);
        Page<UserDto> resDto = res.map(userMapper::toDto);
        return ResponseEntity.ok(resDto);
    }

    // TODO: Miks see päring töötab?
    @Operation(summary = "Save a new user")
    @RolesAllowed({ROLE_ADMIN, ROLE_REGULAR})
    @PostMapping
    public ResponseEntity<UserDto> save(@Valid @RequestBody UserDto userDto) {
        log.info("REST request to save user " + userDto);
        UserDto createdUser = userMapper.toDto(userService.save(userDto));
        return created(URI.create("/api/users/%s".formatted(createdUser.getId()))).body(createdUser);
    }

    @PutMapping
    @Operation(summary = "Update an existing user")
    public ResponseEntity<UserDto> update(@Valid @RequestBody UserDto userDto) {
        log.info("REST request to update user " + userDto);
        User user = userMapper.toEntity(userDto);
        return ResponseEntity.ok(userMapper.toDto(userService.update(user)));
    }
    @GetMapping("/access-token")
    public ResponseEntity<String> getAccessToken(JwtAuthenticationToken token) {
        log.info("REST request to get access token");
        String userId = token.getTokenAttributes().get("sub").toString();
        log.info("User id: " + userId);
        boolean isAuthenticated = token.isAuthenticated();
        if (isAuthenticated) {
            return ResponseEntity.ok("Authenticated");
        } else {
            return ResponseEntity.ok("Not authenticated");
        }
    }

    @Secured(ROLE_ADMIN)
    @Operation(summary = "Delete an existing user")
    @DeleteMapping(path = "/{userId}")
    public void delete(@PathVariable Long userId) {
        log.info("REST request to delete user " + userId);
        userService.delete(userId);
    }
}
