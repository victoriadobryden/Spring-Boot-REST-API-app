package com.example.taskblock2.repositories;

import com.example.taskblock2.data.Case;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomCaseRepository {

    Page<Case> findAllWithInvestigator(Specification<Case> spec, Pageable pageable);

    List<Case> findAllWithInvestigator(Specification<Case> spec);
}
