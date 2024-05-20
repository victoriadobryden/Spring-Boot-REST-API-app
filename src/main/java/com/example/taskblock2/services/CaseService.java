package com.example.taskblock2.services;

import com.example.taskblock2.data.Case;
import com.example.taskblock2.repositories.CaseRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class CaseService {

    @Autowired
    private CaseRepository caseRepository;

    public List<Case> findAll() {
        return caseRepository.findAll();
    }

    public java.util.Optional<Case> findById(Long id) {
        return caseRepository.findById(id);
    }

    public Case save(Case aCase) {
        return caseRepository.save(aCase);
    }

    public void deleteById(Long id) {
        caseRepository.deleteById(id);
    }

    public ImportResult importCases(MultipartFile file) {
        ImportResult result = new ImportResult();
        List<String> errorMessages = new ArrayList<>();
        int successCount = 0;
        int failureCount = 0;

        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CSVParser csvParser = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim()
                    .parse(reader);

            for (CSVRecord record : csvParser) {
                try {
                    Case aCase = new Case();
                    aCase.setPlaceOfEvent(record.get("placeOfEvent"));
                    aCase.setDate(Date.valueOf(record.get("date")));
                    aCase.setNamesOfVictims(List.of(record.get("namesOfVictims").split(",")));
                    aCase.setCharges(List.of(record.get("charges").split(",")));
                    // Set investigator if needed
                    // aCase.setInvestigator(investigator); // If you have a way to determine the investigator

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
