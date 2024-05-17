package com.example.taskblock2.repositories;

import com.example.taskblock2.data.Case;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CaseRepository extends JpaRepository<Case, Long> {
    Page<Case> findByInvestigatorId(Long investigatorId, PageRequest pageRequest);
}
