package grp.javatemplate.config;

import grp.javatemplate.controller.dto.UserDto;
import grp.javatemplate.model.enums.UserRole;
import grp.javatemplate.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class StartupLogger {
    private final Environment env;

    @Autowired
    UserService userService;

    @PostConstruct
    public void initApplication() {
        List<String> profiles = Arrays.asList(env.getActiveProfiles());

        // App default user
        if (userService.findAll("", 0, 10).isEmpty()){
            userService.save(new UserDto().setName("test").setEmail("test@email.com").setDob(Instant.now()).setRole(UserRole.Roles.REGULAR));
        }

        if (profiles.contains("local")) {
            log.info("App hosted at " + "http://localhost:" + env.getProperty("server.port"));
            log.info("local profiles active");
            log.info("Swagger enabled at http://localhost:8880/api/swagger-ui/index.html#/");
            log.info("Keycloak hosted at http://localhost:9080/keycloak/");
        }
        else if (profiles.contains("develop")){
            log.info("develop profile active");
        }
    }
}
