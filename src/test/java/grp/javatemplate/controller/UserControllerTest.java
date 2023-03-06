package grp.javatemplate.controller;

import grp.javatemplate.controller.dto.UserDto;
import grp.javatemplate.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static grp.javatemplate.TestObjects.createUser;
import static grp.javatemplate.TestObjects.createUserDto;
import static grp.javatemplate.exception.BusinessException.ID_MUST_NOT_BE_NULL;
import static grp.javatemplate.exception.UserException.USER_DOES_NOT_EXIST;
import static grp.javatemplate.exception.UserException.USER_DUPLICATE_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
class UserControllerTest extends BaseIntegrationTest {

    // GET tests
    @Test
    void findAll_shouldReturnAllUsersOrderedById() throws Exception {
        User secondUser = createUser().setName("Bob");
        entityManager.persist(secondUser);

        User firstUser = createUser().setName("Alice");
        entityManager.persist(firstUser);

        mockMvc.perform(get(endpointProperties.getUsers()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Bob"));
    }

    @Test
    void findAll_shouldReturnAllUsersOrderedByName() throws Exception {
        User secondUser = createUser().setName("Bob");
        entityManager.persist(secondUser);

        User firstUser = createUser().setName("Alice");
        entityManager.persist(firstUser);

        mockMvc.perform(get(endpointProperties.getUsers() + "?sortBy=name"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Alice"));
    }

    // SAVE tests
    @Test
    void save_shouldSaveUser() throws Exception {
        UserDto userDto = createUserDto().setName("Alice");
        byte[] bytes = getBytes(userDto);

        mockMvc.perform(post(endpointProperties.getUsers())
                .contentType(MediaType.APPLICATION_JSON)
                .content(bytes))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.dob").value(userDto.getDob().toString()));
    }

    @Test
    void save_shouldReturn400IfUserExists() throws Exception {
        User user = createUser().setName("Alice");
        entityManager.persist(user);

        UserDto userDto = createUserDto().setName("Alice");
        byte[] bytes = getBytes(userDto);

        mockMvc.perform(post(endpointProperties.getUsers())
                .contentType(MediaType.APPLICATION_JSON)
                .content(bytes))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0]").value(USER_DUPLICATE_NAME));
    }

    // UPDATE tests
    @Test
    void update_shouldUpdateUser() throws Exception {
        User user = createUser().setName("Alice");
        entityManager.persist(user);

        UserDto userDto = userMapper.toDto(user).setName("Bob");
        byte[] bytes = getBytes(userDto);

        mockMvc.perform(put(endpointProperties.getUsers())
                .contentType(MediaType.APPLICATION_JSON)
                .content(bytes))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Bob"));

        List<User> createdUsers = findAll(User.class);
        assertEquals("Bob", createdUsers.get(0).getName());
        assertNotNull(createdUsers.get(0).getDob());
        assertNotNull(createdUsers.get(0).getCreatedAt());
        assertNotNull(createdUsers.get(0).getCreatedBy());
    }

    @Test
    void update_shouldReturn404IfUserNotFound() throws Exception {
        UserDto userDto = createUserDto().setName("Bob").setId(1L);
        byte[] bytes = getBytes(userDto);

        mockMvc.perform(put(endpointProperties.getUsers())
                .contentType(MediaType.APPLICATION_JSON)
                .content(bytes))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0]").value(USER_DOES_NOT_EXIST));
    }

    @Test
    void update_shouldReturn400IfUserIdIsNull() throws Exception {
        UserDto userDto = createUserDto().setName("Alice");
        byte[] bytes = getBytes(userDto);

        mockMvc.perform(put(endpointProperties.getUsers())
                .contentType(MediaType.APPLICATION_JSON)
                .content(bytes))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0]").value(ID_MUST_NOT_BE_NULL));
    }


    // DELETE tests
    @Test
    void delete_shouldDeleteUser() throws Exception {
        User user = createUser().setName("Alice");
        entityManager.persist(user);

        mockMvc.perform(delete(endpointProperties.getUsers() + "/" + user.getId()))
                .andDo(print())
                .andExpect(status().isOk());

        List<User> createdUsers = findAll(User.class);
        assertEquals(0, createdUsers.size());
    }

    @Test
    void delete_shouldReturn404IfUserNotFound() throws Exception {
        mockMvc.perform(delete(endpointProperties.getUsers() + "/1"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0]").value(USER_DOES_NOT_EXIST));
    }
}