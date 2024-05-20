package com.example.taskblock2.services;

import com.example.taskblock2.data.Investigator;
import com.example.taskblock2.repositories.InvestigatorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.sql.Date;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
public class InvestigatorServiceTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private InvestigatorRepository investigatorRepository;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        Investigator investigator = new Investigator();
        investigator.setName("Jane Doe");
        investigator.setDateOfBirth(Date.valueOf("1990-01-01"));
        investigator.setYearsOfWork(5);
        investigatorRepository.save(investigator);
    }

    @Test
    public void testCreateInvestigator() throws Exception {
        String investigatorJson = "{\"name\":\"John Smith\",\"dateOfBirth\":\"1985-05-20\",\"yearsOfWork\":10}";

        mockMvc.perform(post("/api/investigators")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(investigatorJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John Smith"))
                .andExpect(jsonPath("$.dateOfBirth").value("1985-05-20"))
                .andExpect(jsonPath("$.yearsOfWork").value(10));
    }

    @Test
    public void testGetAllInvestigators() throws Exception {
        mockMvc.perform(get("/api/investigators")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Jane Doe"));
    }

    @Test
    public void testUpdateInvestigator() throws Exception {
        Investigator investigator = investigatorRepository.findAll().get(0);
        String updatedInvestigatorJson = "{\"name\":\"Jane Doe Updated\",\"dateOfBirth\":\"1990-01-01\",\"yearsOfWork\":6}";

        mockMvc.perform(put("/api/investigators/" + investigator.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedInvestigatorJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jane Doe Updated"))
                .andExpect(jsonPath("$.dateOfBirth").value("1990-01-01"))
                .andExpect(jsonPath("$.yearsOfWork").value(6));
    }

    @Test
    public void testDeleteInvestigator() throws Exception {
        Investigator investigator = investigatorRepository.findAll().get(0);

        mockMvc.perform(delete("/api/investigators/" + investigator.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/investigators")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
