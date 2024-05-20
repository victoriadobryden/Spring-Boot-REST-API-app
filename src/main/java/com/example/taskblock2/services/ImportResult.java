package com.example.taskblock2.services;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ImportResult {
    private int successCount;
    private int failureCount;
    private List<String> errorMessages;

}
