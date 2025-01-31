package com.example.todoApp.todoAppBackend.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaskDTO {

    private int taskId;
    private String title;
    private  String description;
    private LocalDate dueDate;
    private boolean isCompleted;
}
