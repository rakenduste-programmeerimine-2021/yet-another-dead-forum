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
                0L,
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
                0L,
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
                0L,
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
                0L,
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

    @Test
    @DisplayName("Returns a test user's post count - GET /api/{username}/postcount")
    void getUserPostCount() throws Exception {
        //given
        List<Post> posts = new ArrayList<>();
        posts.add(new Post());
        posts.add(new Post());
        posts.add(new Post());

        when(userService.getUserPostCount(any())).thenReturn((long) posts.size());

        // when then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/TestUser/postcount"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postCount", Matchers.is(3)));
    }

    @Test
    @DisplayName("Returns a test user's thread count - GET /api/{username}/threadcount")
    void getUserThreadCount() throws Exception {
        //given
        List<Thread> threads = new ArrayList<>();
        threads.add(new Thread());
        threads.add(new Thread());
        threads.add(new Thread());
        threads.add(new Thread());
        threads.add(new Thread());

        when(userService.getUserThreadCount(any())).thenReturn((long) threads.size());

        // when then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/TestUser/threadcount"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.threadCount", Matchers.is(5)));
    }

    @Test
    @DisplayName("Returns a test user's visits count - GET /api/{username}/visits")
    void getUserVisitsCount() throws Exception {
        //given
        User user = new User(1L,
                "user1",
                "test1@test.com",
                "aaa",
                new ArrayList<>(),
                "",
                999L,
                "",
                new ArrayList<>(),
                new ArrayList<>());

        when(userService.getUserProfileVisitsCount(any())).thenReturn(user.getVisits());

        // when then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/TestUser/visits"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.visits", Matchers.is(999)));
    }
}