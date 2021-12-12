package ee.tlu.forum.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ee.tlu.forum.model.Maintenance;
import ee.tlu.forum.service.MaintenanceService;
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

import static org.mockito.Mockito.when;

@SpringBootTest
class MaintenanceControllerTest {

    public MockMvc mockMvc;

    @Autowired
    MaintenanceController maintenanceController;

    @MockBean
    public MaintenanceService maintenanceService;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(maintenanceController).build();
    }

    @Test
    @DisplayName("Returns current maintenance - GET /api/maintenance")
    void getMaintenance() throws Exception {
        // given
        Maintenance res = new Maintenance();
        res.setId(1L);
        res.setMessage("Down for emergency maintenance");

        when (maintenanceService.getMaintenance())
                .thenReturn(res);

        // when then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/maintenance"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Down for emergency maintenance")));
    }

    @Test
    @DisplayName("Successfully accepts input and returns given input - PATCH /api/maintenance/edit")
    void editMaintenance() throws Exception {
        // given
        UpdateMaintenanceInput req = new UpdateMaintenanceInput();
        req.setMessage("Distributed Denial of Service in progress - please wait");



        when (maintenanceService.updateMaintenance(req.getMessage()))
                .thenAnswer(Maintenance -> {
                    Maintenance res = new Maintenance();
                    res.setId(1L);
                    res.setMessage(req.getMessage());
                    return res;
                });

        String formJson = new ObjectMapper().writeValueAsString(req);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/api/maintenance/edit")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(formJson);

        // when then
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Distributed Denial of Service in progress - please wait")));
    }

}