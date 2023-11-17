package com.java.cites.model;

import lombok.Data;

@Data
public class ImportSummary {
    private int totalRecords;
    private int successfullyImported;
    private int notImported;

    public void incrementSuccessfullyImported() {
        successfullyImported++;
    }

    public void incrementNotImported() {
        notImported++;
    }
}
