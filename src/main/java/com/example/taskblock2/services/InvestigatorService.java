package com.example.taskblock2.services;

import com.example.taskblock2.data.Investigator;
import com.example.taskblock2.repositories.InvestigatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InvestigatorService {

    @Autowired
    private InvestigatorRepository investigatorRepository;

    public List<Investigator> findAll() {
        return investigatorRepository.findAll();
    }

    public Page<Investigator> findAll(Pageable pageable) {
        return investigatorRepository.findAll(pageable);
    }

    public Optional<Investigator> findById(Long id) {
        return investigatorRepository.findById(id);
    }

    public Investigator save(Investigator investigator) {
        return investigatorRepository.save(investigator);
    }

    public void deleteById(Long id) {
        investigatorRepository.deleteById(id);
    }
}
