package grp.javatemplate.controller;

import grp.javatemplate.controller.dto.UserDto;
import grp.javatemplate.mapper.UserMapper;
import grp.javatemplate.model.User;
import grp.javatemplate.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static grp.javatemplate.model.enums.UserRole.ROLE_REGULAR;
import static org.springframework.http.ResponseEntity.created;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping(path = "${endpoint.users}")
public class UserController {
//    public static final String HAS_AUTHORITY_SCOPE_WRITE = "hasAuthority('SCOPE_write')";
    public static final String HAS_AUTHORITY_SCOPE_WRITE = String.format("hasAuthority(%s)", ROLE_REGULAR);
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public  ResponseEntity<List<UserDto>> findAll(@RequestParam(required = false) String sortBy) {
        log.info("REST request to findAll users");
        List<User> res = userService.findAll(sortBy);
        return ResponseEntity.ok(userMapper.toDto(res));
    }

    @PostMapping
    @PreAuthorize(HAS_AUTHORITY_SCOPE_WRITE)
    public ResponseEntity<UserDto> save(@Valid @RequestBody UserDto userDto) {
        log.info("REST request to save user " + userDto);
        UserDto createdUser = userMapper.toDto(userService.save(userDto));
        return created(URI.create("/api/users/%s".formatted(createdUser.getId()))).body(createdUser);
    }

    @PutMapping
    public ResponseEntity<UserDto> update(@Valid @RequestBody UserDto userDto) {
        log.info("REST request to update user " + userDto);
        User user = userMapper.toEntity(userDto);
        return ResponseEntity.ok(userMapper.toDto(userService.update(user)));
    }

    @DeleteMapping(path = "/{userId}")
    public void delete(@PathVariable Long userId) {
        log.info("REST request to delete user " + userId);
        userService.delete(userId);
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
}
