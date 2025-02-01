package com.example.todoApp.todoAppBackend;

import com.example.todoApp.todoAppBackend.controller.TaskController;
import com.example.todoApp.todoAppBackend.dto.TaskDTO;
import com.example.todoApp.todoAppBackend.entity.Task;
import com.example.todoApp.todoAppBackend.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TaskController.class)
public class TaskControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private TaskService taskService;

    private Task task;
    private TaskDTO taskDTO;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setTaskId(1);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setCompleted(false);

        taskDTO = new TaskDTO();
        taskDTO.setTaskId(1);
        taskDTO.setTitle("Test Task");
        taskDTO.setDescription("Test Description");
        taskDTO.setCompleted(false);
    }

    @Test
    public void testSaveTask() throws Exception {
        when(taskService.saveTask(taskDTO)).thenReturn("Task Test Task added successfully...");

        // Perform the POST request
        mockMvc.perform(post("/api/v1/task/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"taskId\": 1, \"title\": \"Test Task\", \"description\": \"Test Description\", \"completed\": false}")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").value("Task Test Task added successfully..."));
    }

    @Test
    public void testGetAllTasksCompleted() throws Exception {
        List<TaskDTO> taskList = List.of(taskDTO);

        when(taskService.getAllTasksCompleted(false, 0, 5)).thenReturn(taskList);

        mockMvc.perform(get("/api/v1/task/get-all-completed/false")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].title").value("Test Task"));
    }

    @Test
    public void testMarkTaskAsDone() throws Exception {
        when(taskService.markAsDone(1)).thenReturn("Task updated successfully...");

        mockMvc.perform(put("/api/v1/task/1/complete")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").value("Task updated successfully..."));
    }

    @Test
    public void testSaveTask_MissingFields() throws Exception {
        String invalidTaskJson = "{\"title\": \"\", \"description\": \"Test Description\"}";

        mockMvc.perform(post("/api/v1/task/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidTaskJson))
                .andExpect(status().isBadRequest());
    }


}
