package com.example.taskblock2.controllers;

import com.example.taskblock2.data.Case;
import com.example.taskblock2.dto.CaseDto;
import com.example.taskblock2.services.CaseService;
import com.example.taskblock2.services.ImportResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/cases")
public class CaseController {
    @Autowired
    private CaseService caseService;

    @PostMapping
    public ResponseEntity<Case> createCase(@RequestBody CaseDto caseDto) {
        Case savedCase = caseService.save(caseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCase);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Case> getCase(@PathVariable Long id) {
        Optional<Case> sprava = caseService.findById(id);
        return sprava.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Case> updateCase(@PathVariable Long id, @RequestBody CaseDto caseDto) {
        caseDto.setId(id);
        Case updatedCase = caseService.update(caseDto);
        return ResponseEntity.ok(updatedCase);
    }


    @PostMapping("/upload")
    public ResponseEntity<ImportResult> importCases(@RequestParam("file") MultipartFile file) {
        ImportResult result = caseService.importCases(file);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCase(@PathVariable Long id) {
        caseService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/_list")
    public ResponseEntity<Map<String, Object>> getCasesList(@RequestBody Map<String, Object> request) {
        Map<String, Object> filters = (Map<String, Object>) request.get("filters");
        int page = (int) request.get("page");
        int size = (int) request.get("size");

        Map<String, Object> result = caseService.getCasesList(filters, page, size);
        return ResponseEntity.ok(result);
    }

    @PostMapping("_report")
    public ResponseEntity<byte[]> generateCasesReport(@RequestBody Map<String, Object> request) {
        Map<String, Object> filters = (Map<String, Object>) request.get("filters");
        byte[] report = caseService.generateCasesReport(filters);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=cases_report.csv")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(report);
    }

}
