package com.example.todoApp.todoAppBackend;
import com.example.todoApp.todoAppBackend.controller.TaskController;
import com.example.todoApp.todoAppBackend.dto.TaskDTO;
import com.example.todoApp.todoAppBackend.entity.Task;
import com.example.todoApp.todoAppBackend.exception.NotFoundException;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

    /**
     * Set up mock task objects before each test.
     * Ensures consistent test data for all test cases.
     */
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

    /**
     * Test for saving a new task.
     * - Mocks the behavior of taskService.saveTask().
     * - Sends a POST request to `/api/v1/task/save`.
     * - Verifies the response structure and expected success message.
     */
    @Test
    public void testSaveTask() throws Exception {
        when(taskService.saveTask(taskDTO)).thenReturn("Task Test Task added successfully...");

        mockMvc.perform(post("/api/v1/task/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"taskId\": 1, \"title\": \"Test Task\", \"description\": \"Test Description\", \"completed\": false}")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200)) // HTTP 200 OK expected
                .andExpect(jsonPath("$.message").value("Success")) // Response should indicate success
                .andExpect(jsonPath("$.data").value("Task Test Task added successfully...")); // Data should contain the success message
    }

    /**
     * Test for retrieving all incomplete tasks.
     * - Mocks the behavior of taskService.getAllTasksCompleted().
     * - Sends a GET request to `/api/v1/task/get-all-completed/false`.
     * - Verifies that at least one task is returned and matches the expected title.
     */
    @Test
    public void testGetAllTasksCompleted_WhenTasksExist() throws Exception {
        List<TaskDTO> taskList = List.of(taskDTO);
        when(taskService.getAllTasksCompleted(false, 0, 5)).thenReturn(taskList);

        mockMvc.perform(get("/api/v1/task/get-all-completed")
                        .param("status", "false")
                        .param("page", "0")
                        .param("size", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Expecting HTTP 200 OK
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].title").value("Test Task"));
    }
    @Test
    public void testGetAllTasksCompleted_WhenNoTasksExist() throws Exception {
        when(taskService.getAllTasksCompleted(false, 0, 5))
                .thenThrow(new NotFoundException("Not found..."));  // Simulate no tasks scenario properly

        mockMvc.perform(get("/api/v1/task/get-all-completed")
                        .param("status", "false")
                        .param("page", "0")
                        .param("size", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())  // Expecting 404
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("not found"));
    }

    /**
     * Test for marking a task as completed.
     * - Mocks the behavior of taskService.markAsDone().
     * - Sends a PUT request to `/api/v1/task/1/complete`.
     * - Verifies that the response indicates successful update.
     */
    @Test
    public void testMarkTaskAsDone() throws Exception {
        when(taskService.markAsDone(1)).thenReturn("Task updated successfully...");
        mockMvc.perform(put("/api/v1/task/" + task.getTaskId() + "/complete")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Expecting HTTP 200 OK
                .andExpect(jsonPath("$.code").value(200)) // Response code check
                .andExpect(jsonPath("$.message").value("Success")) // Response should indicate success
                .andExpect(jsonPath("$.data").value("Task updated successfully..."));
    }

    /**
     * Test for saving a task with missing required fields.
     * - Sends an invalid POST request with missing `title`.
     * - Verifies that the response returns a 400 Bad Request status.
     */
    @Test
    public void testSaveTask_MissingFields() throws Exception {
        String invalidTaskJson = "{\"title\": \"\", \"description\": \"Test Description\"}";

        mockMvc.perform(post("/api/v1/task/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidTaskJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation Failed"))
                .andExpect(jsonPath("$.data").value("Title is required"));
    }
}
