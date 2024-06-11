package com.example.taskblock2.controllers;

import com.example.taskblock2.data.Investigator;
import com.example.taskblock2.dto.CaseDto;
import com.example.taskblock2.data.Case;
import com.example.taskblock2.services.CaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CaseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CaseService caseService;

    private Case caseEntity;
    private CaseDto caseDto;

    @BeforeEach
    void setUp() {
        Investigator investigator = new Investigator();
        investigator.setId(1L);
        investigator.setName("John Doe");
        investigator.setDateOfBirth(LocalDate.parse("1979-12-31"));
        investigator.setYearsOfWork(10);

        caseEntity = new Case();
        caseEntity.setId(1L);
        caseEntity.setPlaceOfEvent("Place 1");
        caseEntity.setNamesOfVictims(Collections.singletonList("Victim 1"));
        caseEntity.setCharges(Collections.singletonList("Charge 1"));
        caseEntity.setInvestigator(investigator);

        caseDto = new CaseDto();
        caseDto.setPlaceOfEvent("Place 1");
        caseDto.setNamesOfVictims(Collections.singletonList("Victim 1"));
        caseDto.setCharges(Collections.singletonList("Charge 1"));
        caseDto.setInvestigatorId(1L);
    }

    @Test
    void testCreateCase() throws Exception {
        Mockito.when(caseService.save(Mockito.any(CaseDto.class))).thenReturn(caseEntity);

        mockMvc.perform(post("/api/cases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"placeOfEvent\":\"Place 1\",\"namesOfVictims\":[\"Victim 1\"],\"charges\":[\"Charge 1\"],\"investigatorId\":1}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.placeOfEvent", is("Place 1")));
    }

    @Test
    void testGetCase() throws Exception {
        Mockito.when(caseService.findById(1L)).thenReturn(Optional.of(caseEntity));

        mockMvc.perform(get("/api/cases/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.placeOfEvent", is("Place 1")));
    }

    @Test
    void testUpdateCase() throws Exception {
        caseEntity.setPlaceOfEvent("Updated Place");
        Mockito.when(caseService.update(Mockito.any(CaseDto.class))).thenReturn(caseEntity);

        mockMvc.perform(put("/api/cases/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"placeOfEvent\":\"Updated Place\",\"namesOfVictims\":[\"Victim 1\"],\"charges\":[\"Charge 1\"],\"investigatorId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.placeOfEvent", is("Updated Place")));
    }

    @Test
    void testDeleteCase() throws Exception {
        mockMvc.perform(delete("/api/cases/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetCasesList() throws Exception {
        Mockito.when(caseService.getCasesList(Mockito.anyMap(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(Collections.singletonMap("list", Collections.singletonList(caseEntity)));

        mockMvc.perform(post("/api/cases/_list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"filters\":{},\"page\":1,\"size\":10}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.list[0].placeOfEvent", is("Place 1")));
    }

    @Test
    void testGenerateCasesReport() throws Exception {
        byte[] reportContent = "ID,Date,Place of Event,Names of Victims,Charges\n1,,Place 1,Victim 1,Charge 1\n".getBytes();
        Mockito.when(caseService.generateCasesReport(Mockito.anyMap())).thenReturn(reportContent);

        mockMvc.perform(post("/api/cases/_report")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"filters\":{}}"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=cases_report.csv"))
                .andExpect(content().bytes(reportContent));
    }

    @Test
    void testGetCasesListWithFilters() throws Exception {
        Map<String, Object> filters = new HashMap<>();
        filters.put("investigatorId", 1);
        filters.put("date", "2024-01-01");
        filters.put("placeOfEvent", "Place 1");

        Mockito.when(caseService.getCasesList(Mockito.anyMap(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(Collections.singletonMap("list", Collections.singletonList(caseEntity)));

        mockMvc.perform(post("/api/cases/_list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"filters\":{\"investigatorId\":1,\"date\":\"2024-01-01\",\"placeOfEvent\":\"Place 1\"},\"page\":1,\"size\":10}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.list[0].placeOfEvent", is("Place 1")));
    }

    @Test
    void testGenerateCasesReportWithFilters() throws Exception {
        Map<String, Object> filters = new HashMap<>();
        filters.put("investigatorId", 1);
        filters.put("date", "2024-01-01");
        filters.put("placeOfEvent", "Place 1");

        byte[] reportContent = "ID,Date,Place of Event,Names of Victims,Charges\n1,,Place 1,Victim 1,Charge 1\n".getBytes();
        Mockito.when(caseService.generateCasesReport(Mockito.anyMap())).thenReturn(reportContent);

        mockMvc.perform(post("/api/cases/_report")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"filters\":{\"investigatorId\":1,\"date\":\"2024-01-01\",\"placeOfEvent\":\"Place 1\"}}"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=cases_report.csv"))
                .andExpect(content().bytes(reportContent));
    }
}
