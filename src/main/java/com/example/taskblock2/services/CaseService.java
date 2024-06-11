package com.example.taskblock2.services;

import com.example.taskblock2.data.Case;
import com.example.taskblock2.data.Investigator;
import com.example.taskblock2.dto.CaseDto;
import com.example.taskblock2.repositories.CaseRepository;
import com.example.taskblock2.repositories.CustomCaseRepositoryImpl;
import com.example.taskblock2.repositories.InvestigatorRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
public class CaseService {
    private static final Logger logger = LoggerFactory.getLogger(CaseService.class);

    @Autowired
    private CaseRepository caseRepository;

    @Autowired
    private InvestigatorRepository investigatorRepository;
    @Autowired
    private CustomCaseRepositoryImpl customCaseRepositoryImpl;

    public List<Case> findAll() {
        return caseRepository.findAll();
    }

    public Optional<Case> findById(Long id) {
        return caseRepository.findById(id);
    }

    public Case save(Case sprava) {
        return caseRepository.save(sprava);
    }

    public void deleteById(Long id) {
        caseRepository.deleteById(id);
    }

    public Case save(CaseDto caseDto) {
        Optional<Case> caseOpt = caseRepository.findById(caseDto.getId());
        Case sprava = caseOpt.orElse(new Case());

        sprava.setDate(caseDto.getDate());
        sprava.setPlaceOfEvent(caseDto.getPlaceOfEvent());
        sprava.setNamesOfVictims(caseDto.getNamesOfVictims());
        sprava.setCharges(caseDto.getCharges());

        Optional<Investigator> investigatorOpt = investigatorRepository.findById(caseDto.getInvestigatorId());

        if (investigatorOpt.isPresent()) {
            sprava.setInvestigator(investigatorOpt.get());
        } else {
            throw new RuntimeException("Investigator with ID " + caseDto.getInvestigatorId() + " not found");
        }

        // Save the case entity
        return caseRepository.save(sprava);
    }


    public Case update(CaseDto caseDto) {
        Optional<Case> caseOpt = caseRepository.findById(caseDto.getId());
        if (caseOpt.isEmpty()) {
            throw new RuntimeException("Case with ID " + caseDto.getId() + " not found");
        }

        Case sprava = caseOpt.get();
        sprava.setDate(caseDto.getDate());
        sprava.setPlaceOfEvent(caseDto.getPlaceOfEvent());
        sprava.setNamesOfVictims(caseDto.getNamesOfVictims());
        sprava.setCharges(caseDto.getCharges());

        Optional<Investigator> investigatorOpt = investigatorRepository.findById(caseDto.getInvestigatorId());

        if (investigatorOpt.isPresent()) {
            sprava.setInvestigator(investigatorOpt.get());
        } else {
            throw new RuntimeException("Investigator with ID " + caseDto.getInvestigatorId() + " not found");
        }

        return caseRepository.save(sprava);
    }

    public ImportResult importCases(MultipartFile file) {
        ImportResult result = new ImportResult();
        List<String> errorMessages = new ArrayList<>();
        int successCount = 0;
        int failureCount = 0;

        try (InputStream inputStream = file.getInputStream()) {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, Object>> records = objectMapper.readValue(inputStream, new TypeReference<>() {});

            for (Map<String, Object> record : records) {
                try {
                    Case sprava = new Case();
                    Long investigatorId = Long.valueOf(record.get("investigatorId").toString());

                    Optional<Investigator> investigatorOpt = investigatorRepository.findById(investigatorId);

                    if (investigatorOpt.isPresent()) {
                        sprava.setInvestigator(investigatorOpt.get());
                    } else {
                        throw new Exception("Investigator with ID " + investigatorId + " not found");
                    }

                    sprava.setPlaceOfEvent(record.get("placeOfEvent").toString());
                    sprava.setDate(LocalDate.parse(record.get("date").toString()));
                    sprava.setNamesOfVictims((List<String>) record.get("namesOfVictims"));
                    sprava.setCharges((List<String>) record.get("charges"));

                    caseRepository.save(sprava);
                    successCount++;
                } catch (Exception e) {
                    errorMessages.add("Error processing record: " + record + " - " + e.getMessage());
                    failureCount++;
                }
            }
        } catch (IOException e) {
            errorMessages.add("Error reading file: " + e.getMessage());
            failureCount++;
        }

        result.setSuccessCount(successCount);
        result.setFailureCount(failureCount);
        result.setErrorMessages(errorMessages);

        return result;
    }

    public Map<String, Object> getCasesList(Map<String, Object> filters, int page, int size) {
        logger.info("Received filters: {}", filters);
        logger.info("Page: {}, Size: {}", page, size);
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Specification<Case> spec = createSpecification(filters);
        Page<Case> casesPage = customCaseRepositoryImpl.findAllWithInvestigator(spec, pageRequest);
        Map<String, Object> result = new HashMap<>();
        result.put("list", casesPage.getContent());
        result.put("totalPages", casesPage.getTotalPages());
        return result;
    }

    private Specification<Case> createSpecification(Map<String, Object> filters) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filters != null) {
                if (filters.containsKey("investigatorId") && filters.get("investigatorId") != null) {
                    Long investigatorId = Long.valueOf(filters.get("investigatorId").toString());
                    logger.info("Filtering by investigator_id: {}", investigatorId);
                    predicates.add(cb.equal(root.get("investigator").get("id"), investigatorId));
                }

                if (filters.containsKey("date") && filters.get("date") != null) {
                    try {
                        String dateString = filters.get("date").toString();
                        logger.info("Filtering by date: {}", dateString);
                        LocalDate localDate = LocalDate.parse(dateString);
                        predicates.add(cb.equal(root.get("date"), localDate));
                    } catch (DateTimeParseException e) {
                        throw new RuntimeException("Invalid date format. Please use 'yyyy-MM-dd'.");
                    }
                }

                if (filters.containsKey("placeOfEvent") && filters.get("placeOfEvent") != null) {
                    String placeOfEvent = filters.get("placeOfEvent").toString();
                    logger.info("Filtering by placeOfEvent: {}", placeOfEvent);
                    predicates.add(cb.like(root.get("placeOfEvent"), "%" + placeOfEvent + "%"));
                }
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }


    public byte[] generateCasesReport(Map<String, Object> filters) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try (CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), CSVFormat.DEFAULT)) {
            printer.printRecord("ID", "Date", "Place of Event", "Names of Victims", "Charges");

            Specification<Case> spec = createSpecification(filters);
            List<Case> cases = customCaseRepositoryImpl.findAllWithInvestigator(spec);

            for (Case sprava : cases) {
                printer.printRecord(
                        sprava.getId(),
                        sprava.getDate().toString(),
                        sprava.getPlaceOfEvent(),
                        String.join(", ", sprava.getNamesOfVictims()),
                        String.join(", ", sprava.getCharges())
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return out.toByteArray();
    }
}


