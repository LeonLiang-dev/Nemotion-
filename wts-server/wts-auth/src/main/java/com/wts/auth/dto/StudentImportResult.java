package com.wts.auth.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class StudentImportResult {
    private int total;
    private int created;
    private int updated;
    private int failed;
    private List<String> errors = new ArrayList<>();

    public void addCreated() {
        total++;
        created++;
    }

    public void addUpdated() {
        total++;
        updated++;
    }

    public void addError(String error) {
        total++;
        failed++;
        errors.add(error);
    }
}
