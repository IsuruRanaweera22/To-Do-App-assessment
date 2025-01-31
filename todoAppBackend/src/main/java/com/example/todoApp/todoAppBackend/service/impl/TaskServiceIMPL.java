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
    public List<TaskDTO> getAllTasksCompleted(boolean b) {
        List<Task> allTasks = taskRepo.findAllByisCompletedEquals(b);
        List<TaskDTO> returnedTasks = modelMapper.map(allTasks , new TypeToken<List<TaskDTO>>(){}.getType());
        if(!returnedTasks.isEmpty()){
            return returnedTasks;
        }else{
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
