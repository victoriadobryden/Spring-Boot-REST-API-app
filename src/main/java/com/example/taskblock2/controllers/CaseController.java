package com.example.taskblock2.controllers;

import com.example.taskblock2.data.Case;
import com.example.taskblock2.services.CaseService;
import com.example.taskblock2.services.ImportResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

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
        Optional<Case> aCase = caseService.findById(id);
        return aCase.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Case> updateCase(@PathVariable Long id, @RequestBody Case aCase) {
        aCase.setId(id);
        Case updatedCase = caseService.save(aCase);
        return ResponseEntity.ok(updatedCase);
    }

    @PostMapping("/import")
    public ResponseEntity<ImportResult> importCases(@RequestParam("file") MultipartFile file) {
        ImportResult result = caseService.importCases(file);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCase(@PathVariable Long id) {
        caseService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
