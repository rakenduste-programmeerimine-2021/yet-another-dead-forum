package ee.tlu.forum.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ee.tlu.forum.model.Post;
import ee.tlu.forum.model.Role;
import ee.tlu.forum.model.Thread;
import ee.tlu.forum.model.User;
import ee.tlu.forum.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserControllerTest {

    public MockMvc mockMvc;

    @Autowired
    UserController userController;

    @MockBean
    public UserService userService;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    @DisplayName("Returns a list of users - GET /api/users")
    void getUsers() throws Exception {
        //given
        Role role1 = new Role(1L, "Test1");
        Collection<Role> roles = Arrays.asList(role1);

        User user = new User(1L,
                "user1",
                "test1@test.com",
                "aaa",
                roles,
                "",
                "",
                new ArrayList<>(),
                new ArrayList<>());

        when(userService.getUsers()).thenReturn(Arrays.asList(user));

        // when then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].roles.size()", Matchers.is(1)));
    }

    @Test
    @DisplayName("Returns registered user as response at POST - /api/register")
    void registerUser() throws Exception {
        Role role1 = new Role(1L, "Test1");
        Collection<Role> roles = Arrays.asList(role1);
        User user = new User(1L,
                "user1",
                "test1@test.com",
                "123123123",
                roles,
                "",
                "",
                new ArrayList<>(),
                new ArrayList<>());
        Map<String, String> form = new HashMap<>();
        form.put("username", "user1");
        form.put("email", "test@test.com");
        form.put("password", "123123123");

        String formToJson = new ObjectMapper().writeValueAsString(form);
        when (userService.registerUser(any()))
                .thenReturn(user);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/register")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(formToJson);

        // when then
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().is(201))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", Matchers.is("user1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.roles.size()", Matchers.is(1)));
    }

    @Test
    @DisplayName("Returns back the user as response at POST - /api/user/edit")
    void saveUser() throws Exception {
        // given
        User user = new User(1L,
                "user1",
                "test1@test.com",
                "aaa",
                new ArrayList<>(),
                "",
                "",
                new ArrayList<>(),
                new ArrayList<>());
        String userToJson = new ObjectMapper().writeValueAsString(user);
        when (userService.editUser(any(User.class)))
                .thenReturn(user);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/user/edit")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(userToJson);

        // when then
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", Matchers.is("user1")));
    }

    @Test
    @DisplayName("Returns a user by ID - GET /api/user/{id}")
    void getUserById() throws Exception {
        //given
        User user = new User(1L,
                "user1",
                "test1@test.com",
                "aaa",
                new ArrayList<>(),
                "",
                "",
                new ArrayList<>(),
                new ArrayList<>());

        when(userService.getUserById(any())).thenReturn(user);

        // when then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/1"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", Matchers.is("user1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.roles.size()", Matchers.is(0)));
    }
}