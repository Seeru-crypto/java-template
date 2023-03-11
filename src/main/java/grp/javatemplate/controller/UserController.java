package grp.javatemplate.controller;

import grp.javatemplate.config.security.RoleConstants;
import grp.javatemplate.controller.dto.UserDto;
import grp.javatemplate.mapper.UserMapper;
import grp.javatemplate.model.User;
import grp.javatemplate.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;
import java.util.List;

import static org.springframework.http.ResponseEntity.created;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping(path = "${endpoint.users}")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public  ResponseEntity<List<UserDto>> findAll(@RequestParam(required = false) String sortBy) {
        log.info("REST request to findAll users");
        List<User> res = userService.findAll(sortBy);
        return ResponseEntity.ok(userMapper.toDto(res));
    }

    @PostMapping
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
    @PreAuthorize("hasAuthority(\"" + RoleConstants.ROLE_ADMIN + "\")")
    public void delete(@PathVariable Long userId) {
        log.info("REST request to delete user " + userId);
        userService.delete(userId);
    }

    @GetMapping(path = "/logout")
    public String logout(HttpServletRequest request) throws ServletException {
        request.logout();
        return "/";
    }

    @GetMapping("/access-token")
    public ResponseEntity<String> getAccessToken(JwtAuthenticationToken token) {
        boolean isAuthenticated = token.isAuthenticated();
        if (isAuthenticated) {
            return ResponseEntity.ok("Authenticated");
        } else {
            return ResponseEntity.ok("Not authenticated");
        }
    }
    // TODO: https://github.com/ch4mpy/spring-addons/tree/master/samples/tutorials
}
