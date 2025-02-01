package com.example.todoApp.todoAppBackend.service;

import com.example.todoApp.todoAppBackend.dto.TaskDTO;

import java.util.List;

public interface TaskService {
    public String saveTask(TaskDTO taskDTO);

    String markAsDone(Integer id);

    List<TaskDTO> getAllTasksCompleted(boolean status, int page, int size);
}