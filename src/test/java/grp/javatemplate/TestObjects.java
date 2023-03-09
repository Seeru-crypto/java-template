package grp.javatemplate;

import grp.javatemplate.controller.dto.UserDto;
import grp.javatemplate.model.User;
import grp.javatemplate.model.enums.UserRole;
import jakarta.persistence.EntityManager;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class TestObjects {
    public static final String USER_NAME = "Alice";
    public static final String USER_EMAIL = "email@gmail.com";
    public static final String USER_NAME_2 = "Bob";

    private static final List<Object> createdEntities = new ArrayList<>();

    public static UserDto createUserDto() {
        return new UserDto()
                .setName(USER_NAME)
                .setRole(UserRole.REGULAR)
                .setDob(Instant.now().truncatedTo( ChronoUnit.MICROS ));
    }

    public static User createUser() {
        return new User()
                .setName(USER_NAME_2)
                .setEmail(USER_EMAIL)
                .setRole(UserRole.REGULAR)
                .setDob(Instant.now().minus(16L, java.time.temporal.ChronoUnit.DAYS));
    }

    public static void clear() {
        createdEntities.clear();
    }

    public static void persistCreatedEntities( EntityManager em ) {
        createdEntities.forEach(em::persist);
        em.flush();
    }
}
