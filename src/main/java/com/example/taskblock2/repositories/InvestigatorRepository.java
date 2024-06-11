package com.example.taskblock2.repositories;

import com.example.taskblock2.data.Investigator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface InvestigatorRepository extends JpaRepository<Investigator, Long> {
}
