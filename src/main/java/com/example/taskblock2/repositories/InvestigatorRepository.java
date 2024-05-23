package com.example.taskblock2.repositories;

import com.example.taskblock2.data.Investigator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestigatorRepository extends JpaRepository<Investigator, Long> {
}
