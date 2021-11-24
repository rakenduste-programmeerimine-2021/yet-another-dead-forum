package ee.tlu.forum.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ee.tlu.forum.model.Role;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class RoleControllerTest {

    public MockMvc mockMvc;

    @Autowired
    RoleController roleController;

    @MockBean
    public UserService userService;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(roleController).build();
    }

    @Test
    @DisplayName("List all roles when making GET - /api/roles")
    void getRoles() throws Exception {
        // given
        Role role1 = new Role(1L, "Test1");
        Role role2 = new Role(2L, "Test2");
        Role role3 = new Role(3L, "Test3");
        List<Role> roles = new ArrayList<>();
        roles.add(role1);
        roles.add(role2);
        roles.add(role3);

        when (userService.getRoles())
                .thenReturn(roles);

        // when then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/roles"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is("Test1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].name", Matchers.is("Test3")));
    }

    @Test
    @DisplayName("Return new role object when creating a new role at POST - /api/role/save")
    void saveRole() throws Exception {
        // given
        Role role1 = new Role(1L, "SOME_ROLE");
        String roleToJson = new ObjectMapper().writeValueAsString(role1);
        when (userService.saveRole(any(Role.class)))
                .thenReturn(role1);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/role/save")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(roleToJson);

        // when then
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().is(201))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("SOME_ROLE")));
    }
}