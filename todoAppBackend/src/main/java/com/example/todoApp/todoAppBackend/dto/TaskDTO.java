package com.example.todoApp.todoAppBackend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaskDTO {
    private int taskId;
    @NotBlank(message = "Title is required")
    private String title;
    @NotBlank(message = "Description is required")
    private  String description;
    private LocalDate dueDate;
    private boolean isCompleted;
    private LocalDateTime  createdAt;
    public TaskDTO(String title, String description, LocalDate dueDate, Boolean isCompleted, LocalDateTime createdAt) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.isCompleted = isCompleted;
        this.createdAt = createdAt;
    }
}
