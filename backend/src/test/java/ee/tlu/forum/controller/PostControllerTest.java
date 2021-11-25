package ee.tlu.forum.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ee.tlu.forum.model.Post;
import ee.tlu.forum.model.Thread;
import ee.tlu.forum.model.User;
import ee.tlu.forum.model.input.AddNewPostInput;
import ee.tlu.forum.service.PostService;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class PostControllerTest {

    public MockMvc mockMvc;

    @Autowired
    PostController postController;

    @MockBean
    public PostService postService;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(postController).build();
    }

    @Test
    @DisplayName("Returns a list of posts at - GET /api/threads")
    void getPosts() throws Exception {
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
        Thread t1 = new Thread(1L, "content", "title", user, new ArrayList<>());
        Post p1 = new Post(1L, "text", user, t1);

        when (postService.getAllPosts())
                .thenReturn(Arrays.asList(p1));

        // when then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/posts"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].text", Matchers.is("text")));
    }

    @Test
    @DisplayName("Returns a post by ID at - GET /api/thread/{id}")
    void getPostById() throws Exception {
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
        Thread t1 = new Thread(1L, "content", "title", user, new ArrayList<>());
        Post p1 = new Post(1L, "text", user, t1);

        when (postService.getPostById(any()))
                .thenReturn(p1);

        // when then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/post/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text", Matchers.is("text")));
    }

    @Test
    @DisplayName("Returns created post - POST /api/post/add")
    void createPost() throws Exception {
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
        Thread t1 = new Thread(1L, "content", "title", user, new ArrayList<>());
        Post p1 = new Post(1L, "text", user, t1);

        AddNewPostInput form = new AddNewPostInput();
        form.setUsername("user1");
        form.setText("text");
        String formJson = new ObjectMapper().writeValueAsString(form);

        when (postService.createPost(any(AddNewPostInput.class)))
                .thenReturn(p1);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/post/add")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(formJson);

        // when then
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().is(201))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text", Matchers.is("text")));
    }

    @Test
    @DisplayName("Returns edited post - PATCH /api/post/edit")
    void editPost() throws Exception {
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
        Thread t1 = new Thread(1L, "content", "title", user, new ArrayList<>());
        Post p1 = new Post(1L, "text", user, t1);

        Map<String, String> form = new HashMap<>();
        form.put("title", t1.getTitle());
        String formJson = new ObjectMapper().writeValueAsString(form);

        when (postService.editPost(any()))
                .thenReturn(p1);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/api/post/edit")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(formJson);

        // when then
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text", Matchers.is("text")));
    }

    @Test
    @DisplayName("Returns a list of posts belonging to a user by user id - GET /api/posts/userid/{id}")
    void getPostsByUserId() throws Exception {
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
        Thread t1 = new Thread(1L, "content", "title", user, new ArrayList<>());
        Post p1 = new Post(1L, "text", user, t1);
        Post p2 = new Post(2L, "text", user, t1);
        Collection<Post> posts = user.getPosts();
        posts.add(p1);
        posts.add(p2);
        user.setPosts(posts);

        when (postService.getAllPostsByUserId(any()))
                .thenReturn((List<Post>) user.getPosts());
        // when then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/userid/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].text", Matchers.is("text")));
    }

    @Test
    @DisplayName("Returns a list of posts belonging to a user by username - GET /api/posts/userid/{id}")
    void getPostsByUsername() throws Exception {
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
        Thread t1 = new Thread(1L, "content", "title", user, new ArrayList<>());
        Post p1 = new Post(1L, "text", user, t1);
        Post p2 = new Post(2L, "text", user, t1);
        Collection<Post> posts = user.getPosts();
        posts.add(p1);
        posts.add(p2);
        user.setPosts(posts);

        when (postService.getAllPostsByUsername(any()))
                .thenReturn((List<Post>) user.getPosts());
        // when then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/username/user1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].text", Matchers.is("text")));
    }
}