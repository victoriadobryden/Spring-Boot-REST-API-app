package com.example.taskblock2.controllers;

import com.example.taskblock2.data.Investigator;
import com.example.taskblock2.services.InvestigatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class InvestigatorControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InvestigatorService investigatorService;

    private Investigator investigator;

    @BeforeEach
    void setUp() {
        investigator = new Investigator();
        investigator.setId(1L);
        investigator.setName("John Doe");
        investigator.setDateOfBirth(LocalDate.parse("1979-12-31"));
        investigator.setYearsOfWork(10);
    }

    @Test
    void testCreateInvestigator() throws Exception {
        Mockito.when(investigatorService.save(Mockito.any(Investigator.class))).thenReturn(investigator);

        mockMvc.perform(post("/api/investigators")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Doe\",\"dateOfBirth\":\"1979-12-31\",\"yearsOfWork\":10}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("John Doe")));
    }

    @Test
    void testGetAllInvestigators() throws Exception {
        Page<Investigator> page = new PageImpl<>(Collections.singletonList(investigator));
        Mockito.when(investigatorService.findAll(Mockito.any(PageRequest.class))).thenReturn(page);

        mockMvc.perform(get("/api/investigators?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("John Doe")));
    }

    @Test
    void testUpdateInvestigator() throws Exception {
        investigator.setName("Updated Name");
        Mockito.when(investigatorService.save(Mockito.any(Investigator.class))).thenReturn(investigator);

        mockMvc.perform(put("/api/investigators/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Name\",\"dateOfBirth\":\"1979-12-31\",\"yearsOfWork\":10}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Name")));
    }

    @Test
    void testDeleteInvestigator() throws Exception {
        mockMvc.perform(delete("/api/investigators/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetInvestigatorById() throws Exception {
        Mockito.when(investigatorService.findById(1L)).thenReturn(Optional.of(investigator));

        mockMvc.perform(get("/api/investigators/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("John Doe")));
    }
}