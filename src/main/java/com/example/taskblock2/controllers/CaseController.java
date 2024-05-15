package com.example.taskblock2.controllers;

import com.example.taskblock2.services.CaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cases")
public class CaseController {
    @Autowired
    private CaseService caseService;

    @PostMapping
    public ResponseEntity<Case> createCase(@RequestBody Case aCase) {
        Case savedCase = caseService.save(aCase);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCase);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Case> getCase(@PathVariable Long id) {
        Case aCase = caseService.findById(id);
        return ResponseEntity.ok(aCase);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Case> updateCase(@PathVariable Long id, @RequestBody Case aCase) {
        aCase.setId(id);
        Case updatedCase = caseService.save(aCase);
        return ResponseEntity.ok(updatedCase);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCase(@PathVariable Long id) {
        caseService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Additional endpoints for list, report, etc.
}
