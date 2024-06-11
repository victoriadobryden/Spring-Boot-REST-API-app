package com.example.taskblock2.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class CaseDto {
    private Long id;
    private LocalDate date;
    private String placeOfEvent;
    private Long investigatorId;
    private List<String> namesOfVictims;
    private List<String> charges;
}
