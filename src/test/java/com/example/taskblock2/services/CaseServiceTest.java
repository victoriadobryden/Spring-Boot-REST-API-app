package com.example.taskblock2.services;

import com.example.taskblock2.data.Case;
import com.example.taskblock2.repositories.CaseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class CaseServiceTest {

    @Autowired
    private CaseService caseService;

    @Autowired
    private CaseRepository caseRepository;

    private Case aCase;

    @BeforeEach
    void setUp() {
        aCase = new Case();
        aCase.setPlaceOfEvent("Test Place");
        aCase.setDate(Date.valueOf("2023-01-01"));
        aCase.setNamesOfVictims(Arrays.asList("Victim 1", "Victim 2"));
        aCase.setCharges(Arrays.asList("Charge 1", "Charge 2"));
        aCase = caseRepository.save(aCase);
    }

    @Test
    void testFindAll() {
        List<Case> cases = caseService.findAll();
        assertThat(cases).hasSize(1);
        assertThat(cases.get(0).getPlaceOfEvent()).isEqualTo(aCase.getPlaceOfEvent());
    }

    @Test
    void testFindById() {
        Optional<Case> foundCase = caseService.findById(aCase.getId());
        assertThat(foundCase).isPresent();
        assertThat(foundCase.get().getPlaceOfEvent()).isEqualTo(aCase.getPlaceOfEvent());
    }

    @Test
    void testSave() {
        Case newCase = new Case();
        newCase.setPlaceOfEvent("New Place");
        newCase.setDate(Date.valueOf("2023-02-01"));
        newCase.setNamesOfVictims(Arrays.asList("Victim 3", "Victim 4"));
        newCase.setCharges(Arrays.asList("Charge 3", "Charge 4"));
        Case savedCase = caseService.save(newCase);

        assertThat(savedCase.getPlaceOfEvent()).isEqualTo(newCase.getPlaceOfEvent());
        assertThat(caseRepository.findAll()).hasSize(2);
    }

    @Test
    void testDeleteById() {
        caseService.deleteById(aCase.getId());
        Optional<Case> deletedCase = caseRepository.findById(aCase.getId());
        assertThat(deletedCase).isEmpty();
    }
}
