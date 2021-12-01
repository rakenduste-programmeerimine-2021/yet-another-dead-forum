package ee.tlu.forum.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ee.tlu.forum.model.Post;
import ee.tlu.forum.model.Thread;
import ee.tlu.forum.model.User;
import ee.tlu.forum.model.input.AddNewThreadInput;
import ee.tlu.forum.service.ThreadService;
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
class ThreadControllerTest {
    public MockMvc mockMvc;

    @Autowired
    ThreadController threadController;

    @MockBean
    public ThreadService threadService;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(threadController).build();
    }

    @Test
    @DisplayName("Returns a list of threads at - GET /api/threads")
    void getThreads() throws Exception {
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
        Thread t1 = new Thread(1L, "content", "title", user, new ArrayList<>());
        Thread t2 = new Thread(2L, "content2", "title2", user, new ArrayList<>());

        when (threadService.getAllThreads())
                .thenReturn(Arrays.asList(t1, t2));

        // when then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/threads"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title", Matchers.is("title")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].text", Matchers.is("content2")));
    }

    @Test
    @DisplayName("Returns a thread by ID at - GET /api/threads")
    void getThreadById() throws Exception {
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
        Thread t1 = new Thread(1L, "content", "title", user, new ArrayList<>());

        when (threadService.getThreadById(any()))
                .thenReturn(t1);

        // when then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/thread/1"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is("title")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text", Matchers.is("content")));
    }

    @Test
    @DisplayName("Returns created thread - POST /api/thread/add")
    void createThread() throws Exception {
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
        Thread t1 = new Thread(1L, "content", "title", user, new ArrayList<>());

        AddNewThreadInput form = new AddNewThreadInput();
        form.setUsername("user1");
        form.setTitle("title");
        form.setContent("content");
        String formJson = new ObjectMapper().writeValueAsString(form);

        when (threadService.createThread(any(AddNewThreadInput.class)))
                .thenReturn(t1);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/thread/add")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(formJson);

        // when then
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().is(201))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is("title")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text", Matchers.is("content")));
    }

    @Test
    @DisplayName("Returns edited thread - PATCH /api/thread/edit")
    void editThread() throws Exception {
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
        Thread t1 = new Thread(1L, "content", "title", user, new ArrayList<>());

        Map<String, String> form = new HashMap<>();
        form.put("title", t1.getTitle());
        String formJson = new ObjectMapper().writeValueAsString(form);

        when (threadService.editThread(any()))
                .thenReturn(t1);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/api/thread/edit")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(formJson);

        // when then
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is("title")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text", Matchers.is("content")));
    }


    @Test
    @DisplayName("Returns all posts of a thread edited thread - GET /api/thread/{id}/posts")
    void getAllThreadPosts() throws Exception {
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
        Thread t1 = new Thread(1L, "content", "title", user, new ArrayList<>());
        Post post1 = new Post(1L, "post text", user, t1);
        Collection<Post> posts = t1.getPosts();
        posts.add(post1);
        t1.setPosts(posts);

        when (threadService.getAllPostsByThreadId(any()))
                .thenReturn(t1.getPosts());

        // when then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/thread/1/posts"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].text", Matchers.is("post text")));
    }

    @Test
    @DisplayName("Returns a list of threads belonging to a user by user id - GET /api/threads/userid/{id}")
    void getThreadsByUserId() throws Exception {
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
        Thread t1 = new Thread(1L, "content", "title", user, new ArrayList<>());
        Collection<Thread> threads = user.getThreads();
        threads.add(t1);
        user.setThreads(threads);

        when (threadService.getAllThreadsByUserId(any()))
                .thenReturn((List<Thread>) user.getThreads());

        // when then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/threads/userid/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].text", Matchers.is("content")));
    }

    @Test
    @DisplayName("Returns a list of threads belonging to a user by username - GET /api/threads/username/{name}")
    void getThreadsByUsername() throws Exception {
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
        Thread t1 = new Thread(1L, "content", "title", user, new ArrayList<>());
        Collection<Thread> threads = user.getThreads();
        threads.add(t1);
        user.setThreads(threads);

        when (threadService.getAllThreadsByUsername(any()))
                .thenReturn((List<Thread>) user.getThreads());

        // when then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/threads/username/user1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].text", Matchers.is("content")));
    }
}