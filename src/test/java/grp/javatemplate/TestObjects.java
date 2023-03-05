package grp.javatemplate;

import grp.javatemplate.controller.dto.UserDto;
import grp.javatemplate.model.User;
import grp.javatemplate.model.enums.UserRole;
import jakarta.persistence.EntityManager;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class TestObjects {
    private static final boolean ORDER_PRIORITY = false;
    public static final String ORDER_NUMBER = "NR ABC";

    private static final List<Object> createdEntities = new ArrayList<>();

    public static UserDto createUserDto() {
        return new UserDto()
                .setName("dto name 1")
                .setRole(UserRole.REGULAR);
    }

    public static User createUser() {
        return new User()
                .setName("user name 1")
                .setDob(Instant.now())
                .setRole(UserRole.REGULAR);
    }

    public static void clear() {
        createdEntities.clear();
    }

    public static void persistCreatedEntities( EntityManager em ) {
        createdEntities.forEach(em::persist);
        em.flush();
    }
}
