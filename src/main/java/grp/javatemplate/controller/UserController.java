package grp.javatemplate.controller;

import grp.javatemplate.controller.dto.UserDto;
import grp.javatemplate.mapper.UserMapper;
import grp.javatemplate.model.User;
import grp.javatemplate.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping( path="${endpoint.users}")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public List<UserDto> findAll( @RequestParam(required = false) String sortBy ) {
        log.info("REST request to findAll users");
        List<User> res = userService.findAll(sortBy);
        return userMapper.toDto(res);
    }

    @PostMapping
    public UserDto save( @Valid @RequestBody UserDto userDto, HttpServletResponse response ) {
        log.info("REST request to save user " + userDto);
        response.setStatus(HttpServletResponse.SC_CREATED);
        return userMapper.toDto(userService.save(userDto));
    }

    @PutMapping
    public UserDto update( @Valid @RequestBody UserDto userDto ) {
        log.info("REST request to update user " + userDto);
        User user = userMapper.toEntity(userDto);
        return userMapper.toDto(userService.update(user));
    }

    @DeleteMapping(path = "/{userId}")
    public void delete( @PathVariable Long userId ) {
        log.info("REST request to delete user " + userId);
        userService.delete(userId);
    }

    @GetMapping(path = "/logout")
    public String logout(HttpServletRequest request) throws ServletException {
        request.logout();
        return "/";
    }

    @GetMapping("/anonymous")
    public ResponseEntity<String> getAnonymous() {
        return ResponseEntity.ok("Hello Anonymous");
    }

    @GetMapping("/admin")
    public ResponseEntity<String> getAdmin(Principal principal) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        String userName = (String) token.getTokenAttributes().get("name");
        String userEmail = (String) token.getTokenAttributes().get("email");
        return ResponseEntity.ok("Hello Admin \nUser Name : " + userName + "\nUser Email : " + userEmail);
    }

    @GetMapping("/user")
    public ResponseEntity<String> getUser(Principal principal) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        String userName = (String) token.getTokenAttributes().get("name");
        String userEmail = (String) token.getTokenAttributes().get("email");
        return ResponseEntity.ok("Hello User \nUser Name : " + userName + "\nUser Email : " + userEmail);
    }

}
