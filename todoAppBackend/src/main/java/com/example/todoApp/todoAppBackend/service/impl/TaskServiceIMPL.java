package com.example.todoApp.todoAppBackend.service.impl;

import com.example.todoApp.todoAppBackend.dto.TaskDTO;
import com.example.todoApp.todoAppBackend.entity.Task;
import com.example.todoApp.todoAppBackend.exception.NotFoundException;
import com.example.todoApp.todoAppBackend.repo.TaskRepo;
import com.example.todoApp.todoAppBackend.service.TaskService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceIMPL implements TaskService {

    @Autowired
    ModelMapper modelMapper;
    @Autowired
    TaskRepo taskRepo;

    @Override
    public String saveTask(TaskDTO taskDTO){

        Task task = modelMapper.map(taskDTO,Task.class);
        if(!taskRepo.existsById(task.getTaskId())){
            taskRepo.save(task);
            return "Task " + task.getTitle() + " added successfully...";
        }else{
            throw new DuplicateKeyException("Already added");
        }
    }

    @Override
    public List<TaskDTO> getAllTasksCompleted(boolean status, int page, int size) {
        // Fetch the tasks as a Page object
        Page<Task> allTasks = taskRepo.findAllByisCompletedEquals(status, PageRequest.of(page, size));

        System.out.println("all tasks " + allTasks);

        // Correct mapping: Convert the Page content to a List<TaskDTO>
        List<TaskDTO> returnedTasks = allTasks.getContent().stream()
                .map(task -> modelMapper.map(task, TaskDTO.class)) // Convert each Task to TaskDTO
                .toList(); // Collect as a List

        // Check if the list is empty and throw an exception if it is
        if (!returnedTasks.isEmpty()) {
            return returnedTasks;
        } else {
            throw new RuntimeException("Not found...");
        }
    }



    @Override
    public String markAsDone(Integer id) {
        if(taskRepo.existsById(id)){
            Task task = taskRepo.getByTaskId(id);
            task.setCompleted(true);
            taskRepo.save(task);
            return "Task updated successfully...";
        }else{
            throw new NotFoundException("No users found...s");
        }
    }

    ;
}
