package com.example.todoApp.todoAppBackend.controller;

import com.example.todoApp.todoAppBackend.dto.TaskDTO;
import com.example.todoApp.todoAppBackend.service.TaskService;
import com.example.todoApp.todoAppBackend.utils.StandardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/save")
    public ResponseEntity<StandardResponse> saveTask(@RequestBody TaskDTO taskDTO){
        String saved = taskService.saveTask(taskDTO);
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200, "Success", saved),
                HttpStatus.OK
        );
    }
    @GetMapping(path = "/get-all-completed/", params = {"status", "page", "size"})
    public ResponseEntity<StandardResponse> getAllTasksCompleted(@RequestParam("status") boolean status,
                                                                 @RequestParam("page") int page,
                                                                 @RequestParam("size") int size) {
        System.out.println(status + " " + page + " " + size);
        List<TaskDTO> taskDTO = taskService.getAllTasksCompleted(status, page, size);
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200, "Success", taskDTO),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<StandardResponse> markAsDone(@PathVariable Integer id){
        String result = taskService.markAsDone(id);
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200, "Success", result),
                HttpStatus.OK
        );
    }
}
