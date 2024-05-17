package com.example.taskblock2.services;

import com.example.taskblock2.data.Case;
import com.example.taskblock2.repositories.CaseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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
        aCase.setName("Test Case");
        aCase.setDescription("Test Description");
        aCase = caseRepository.save(aCase);
    }

    @Test
    void testFindAll() {
        List<Case> cases = caseService.findAll();
        assertThat(cases).hasSize(1);
        assertThat(cases.get(0).getName()).isEqualTo(aCase.getName());
    }

    @Test
    void testFindById() {
        Optional<Case> foundCase = caseService.findById(aCase.getId());
        assertThat(foundCase).isPresent();
        assertThat(foundCase.get().getName()).isEqualTo(aCase.getName());
    }

    @Test
    void testSave() {
        Case newCase = new Case();
        newCase.setName("New Case");
        newCase.setDescription("New Description");
        Case savedCase = caseService.save(newCase);

        assertThat(savedCase.getName()).isEqualTo(newCase.getName());
        assertThat(caseRepository.findAll()).hasSize(2);
    }

    @Test
    void testDeleteById() {
        caseService.deleteById(aCase.getId());
        Optional<Case> deletedCase = caseRepository.findById(aCase.getId());
        assertThat(deletedCase).isEmpty();
    }
}
