package com.example.taskblock2.controllers;

import com.example.taskblock2.data.Investigator;
import com.example.taskblock2.services.InvestigatorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/investigators")
public class InvestigatorController {
    @Autowired
    private InvestigatorService investigatorService;

    @PostMapping
    public ResponseEntity<Investigator> createInvestigator(@RequestBody Investigator investigator) {
        Investigator savedInvestigator = investigatorService.save(investigator);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedInvestigator);
    }

    @GetMapping
    public ResponseEntity<List<Investigator>> getAllInvestigators(Pageable pageable) {
        Page<Investigator> investigators = investigatorService.findAll(pageable);
        return ResponseEntity.ok(investigators.getContent());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Investigator> updateInvestigator(@PathVariable Long id, @RequestBody Investigator investigator) {
        investigator.setId(id);
        Investigator updatedInvestigator = investigatorService.save(investigator);
        return ResponseEntity.ok(updatedInvestigator);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvestigator(@PathVariable Long id) {
        investigatorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Investigator> getInvestigatorById(@PathVariable Long id) {
        Optional<Investigator> investigator = investigatorService.findById(id);
        return investigator.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}

