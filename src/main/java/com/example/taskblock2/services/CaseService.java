package com.example.taskblock2.services;

import com.example.taskblock2.data.Case;
import com.example.taskblock2.repositories.CaseRepository;
import com.opencsv.CSVParser;
import jakarta.persistence.EntityNotFoundException;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@Service
public class CaseService {
    @Autowired
    private CaseRepository caseRepository;

    public Case save(Case aCase) {
        return caseRepository.save(aCase);
    }

    public Case findById(Long id) {
        return caseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Case not found"));
    }

    public void deleteById(Long id) {
        caseRepository.deleteById(id);
    }

    public Page<Case> listCases(Long investigatorId, PageRequest pageRequest) {
        return caseRepository.findByInvestigatorId(investigatorId, pageRequest);
    }

    public byte[] generateReport(Long investigatorId) {
        return new byte[0]; // Placeholder
    }

    public ImportResult importCases(MultipartFile file) {
        ImportResult result = new ImportResult();
        List<String> errorMessages = new ArrayList<>();
        int successCount = 0;
        int failureCount = 0;

        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CSVParser csvParser = new CSVFormat.Builder()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .build()
                    .parse(reader);

            for (CSVRecord record : csvParser) {
                try {
                    Case aCase = new Case();
                    aCase.setName(record.get("name"));
                    aCase.setDescription(record.get("description"));
                    // Add other fields as necessary

                    caseRepository.save(aCase);
                    successCount++;
                } catch (Exception e) {
                    errorMessages.add("Error processing record at line " + record.getRecordNumber() + ": " + e.getMessage());
                    failureCount++;
                }
            }
        } catch (Exception e) {
            errorMessages.add("Error reading file: " + e.getMessage());
            failureCount++;
        }

        result.setSuccessCount(successCount);
        result.setFailureCount(failureCount);
        result.setErrorMessages(errorMessages);

        return result;
    }
}
