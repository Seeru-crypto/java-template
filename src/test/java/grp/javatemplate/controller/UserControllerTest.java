package grp.javatemplate.controller;

import grp.javatemplate.controller.dto.UserDto;
import grp.javatemplate.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static grp.javatemplate.exception.BusinessException.ID_MUST_NOT_BE_NULL;
import static grp.javatemplate.exception.UserException.USER_DOES_NOT_EXIST;
import static grp.javatemplate.exception.UserException.USER_DUPLICATE_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
class UserControllerTest extends ContextIntegrationTest {
    // GET tests
    @Test
    void findAll_shouldReturnAllUsers() throws Exception {
        createUser("Bob");
        createUser("Alice");

        mockMvc.perform(get(endpointProperties.getUsers()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.last").value("true"))
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].name").value("Bob"));
    }

    @Test
    void findAll_shouldReturnOrderedUsers() throws Exception {
        createUser("Bob");
        createUser("Alice");

        // Default orderBy is id
        mockMvc.perform(get(endpointProperties.getUsers())
                        .param("pageNumber", "0")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.last").value("true"))
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].name").value("Bob"));

        mockMvc.perform(get(endpointProperties.getUsers())
                        .param("sortBy", "id"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].name").value("Bob"));

        mockMvc.perform(get(endpointProperties.getUsers())
                        .param("sortBy", "name"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].name").value("Alice"));
    }

    @Test
    void findAll_shouldReturnUsersByPageSize() throws Exception {
        createUsers(15);

        mockMvc.perform(get(endpointProperties.getUsers())
                        .param("pageNumber", "0")
                        .param("pageSize", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages").value(15))
                .andExpect(jsonPath("$.totalElements").value(15))
                .andExpect(jsonPath("$.last").value("false"))
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].name").value("User 0"));

        mockMvc.perform(get(endpointProperties.getUsers())
                        .param("pageNumber", "2")
                        .param("pageSize", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages").value(3))
                .andExpect(jsonPath("$.totalElements").value(15))
                .andExpect(jsonPath("$.number").value(2))
                .andExpect(jsonPath("$.last").value("true"))
                .andExpect(jsonPath("$.content.length()").value(5))
                .andExpect(jsonPath("$.content[0].name").value("User 10"));

        mockMvc.perform(get(endpointProperties.getUsers())
                        .param("pageNumber", "-2"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0]").value("Page index must not be less than zero"));

        mockMvc.perform(get(endpointProperties.getUsers())
                        .param("pageSize", "-3"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0]").value("Page size must not be less than one"));
    }

    // SAVE tests
    @Test
    void save_shouldSaveUser() throws Exception {
        UserDto userDto = createUserDto().setName("Alice");
        byte[] bytes = getBytes(userDto);

        mockMvc.perform(post(endpointProperties.getUsers())
                .contentType(MediaType.APPLICATION_JSON)
                .content(bytes))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.dob").value(userDto.getDob().toString()));
    }

    @Test
    void save_shouldReturn400_IfUserExists() throws Exception {
        createUser("Alice");
        UserDto userDto = createUserDto().setName("Alice");

        mockMvc.perform(post(endpointProperties.getUsers())
                .contentType(MediaType.APPLICATION_JSON)
                .content(getBytes(userDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0]").value(USER_DUPLICATE_NAME));
    }

    // UPDATE tests
    @Test
    void update_shouldUpdateUser() throws Exception {
        User user = createUser("Alice");
        UserDto userDto = userMapper.toDto(user).setName("Bob");

        mockMvc.perform(put(endpointProperties.getUsers())
                .contentType(MediaType.APPLICATION_JSON)
                .content(getBytes(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Bob"));

        List<User> createdUsers = findAll(User.class);
        assertEquals("Bob", createdUsers.get(0).getName());
        assertNotNull(createdUsers.get(0).getDob());
        assertNotNull(createdUsers.get(0).getCreatedAt());
        assertNotNull(createdUsers.get(0).getCreatedBy());
    }

    @Test
    void update_shouldReturn404_ifUserNotFound() throws Exception {
        UserDto userDto = createUserDto().setName("Bob").setId(1L);

        mockMvc.perform(put(endpointProperties.getUsers())
                .contentType(MediaType.APPLICATION_JSON)
                .content(getBytes(userDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0]").value(USER_DOES_NOT_EXIST));
    }

    @Test
    void update_shouldReturn400_ifUserIdIsNull() throws Exception {
        UserDto userDto = createUserDto().setName("Alice");

        mockMvc.perform(put(endpointProperties.getUsers())
                .contentType(MediaType.APPLICATION_JSON)
                .content(getBytes(userDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0]").value(ID_MUST_NOT_BE_NULL));
    }

    // DELETE tests
    @Test
    void delete_shouldDeleteUser() throws Exception {
        User user = createUser("Alice");

        mockMvc.perform(delete(String.format("%s/%s",endpointProperties.getUsers(), user.getId())))
                .andExpect(status().isOk());

        List<User> createdUsers = findAll(User.class);
        assertEquals(0, createdUsers.size());
    }

    @Test
    void delete_shouldReturn404_ifUserNotFound() throws Exception {
        mockMvc.perform(delete(String.format("%s/%s",endpointProperties.getUsers(), 1L)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0]").value(USER_DOES_NOT_EXIST));
    }
}