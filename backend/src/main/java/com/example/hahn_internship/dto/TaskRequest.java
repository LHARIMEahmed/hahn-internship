package com.example.hahn_internship.dto;

import java.util.Date;

public record TaskRequest(
    String title,
    String description,
    Date dueDate,
    boolean completed
) {
}
