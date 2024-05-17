package com.example.taskblock2.controllers;

import com.example.taskblock2.data.Investigator;
import com.example.taskblock2.services.InvestigatorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<Investigator>> getAllInvestigators() {
        List<Investigator> investigators = investigatorService.findAll();
        return ResponseEntity.ok(investigators);
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

}
