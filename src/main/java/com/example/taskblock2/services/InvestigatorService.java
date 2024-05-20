package com.example.taskblock2.services;

import com.example.taskblock2.data.Investigator;
import com.example.taskblock2.repositories.InvestigatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvestigatorService {
    @Autowired
    private InvestigatorRepository investigatorRepository;

    public Investigator save(Investigator investigator) {
        return investigatorRepository.save(investigator);
    }

    public List<Investigator> findAll() {
        return investigatorRepository.findAll();
    }

    public void deleteById(Long id) {
        investigatorRepository.deleteById(id);
    }
}
