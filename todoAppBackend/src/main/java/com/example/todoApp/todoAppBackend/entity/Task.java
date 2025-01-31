package com.example.todoApp.todoAppBackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Table(name = "tasks")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Task {

    @Id
    @Column(name = "task_id", length = 100)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int taskId;

    @Column(name = "due_date", length = 255, nullable = false)
    private LocalDate dueDate;

    @Column(name = "title", length = 255, nullable = false)
    private String title;

    @Column(name = "description", length = 255, nullable = false)
    private  String description;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "is_completed", nullable = false, columnDefinition = "TINYINT default 0")
    private boolean isCompleted;

}
