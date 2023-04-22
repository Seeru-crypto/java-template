package grp.javatemplate.controller;

import grp.javatemplate.controller.dto.UserDto;
import grp.javatemplate.model.User;
import grp.javatemplate.model.enums.UserRole;
import jakarta.persistence.EntityManager;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

abstract class ContextIntegrationTest extends BaseIntegrationTest {
    public static final String USER_NAME = "Alice";
    public static final String USER_EMAIL = "email@gmail.com";
    public static final String USER_NAME_2 = "Bob";

    protected User createUser(String name){
        User newUser =  new User()
                .setName(name)
                .setEmail(USER_EMAIL)
                .setRole(UserRole.Roles.REGULAR)
                .setDob(Instant.now().minus(16L, java.time.temporal.ChronoUnit.DAYS));
        entityManager.persist(newUser);
        return newUser;
    }

    protected List<User> createUsers(int count){
        List<User> users = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            String userName = "User " + i;
            users.add(createUser(userName));
        }
        return users;
    }


    private static final List<Object> createdEntities = new ArrayList<>();

    public static UserDto createUserDto() {
        return new UserDto()
                .setName(USER_NAME)
                .setEmail(USER_EMAIL)
                .setRole(UserRole.Roles.REGULAR)
                .setDob(Instant.now().truncatedTo( ChronoUnit.MICROS ));
    }

    public static void clear() {
        createdEntities.clear();
    }

    public static void persistCreatedEntities( EntityManager em ) {
        createdEntities.forEach(em::persist);
        em.flush();
    }





}
