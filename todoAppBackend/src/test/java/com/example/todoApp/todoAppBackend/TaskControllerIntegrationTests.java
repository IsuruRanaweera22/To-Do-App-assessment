package com.example.todoApp.todoAppBackend;

import com.example.todoApp.todoAppBackend.dto.TaskDTO;
import com.example.todoApp.todoAppBackend.entity.Task;
import com.example.todoApp.todoAppBackend.repo.TaskRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TaskControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskRepo taskRepository;

    static MySQLContainer<?> mysql = new MySQLContainer<>(DockerImageName.parse("mysql:8.0"))
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @BeforeAll
    static void startContainer() {
        mysql.start();
        System.setProperty("spring.datasource.url", mysql.getJdbcUrl());
        System.setProperty("spring.datasource.username", mysql.getUsername());
        System.setProperty("spring.datasource.password", mysql.getPassword());
    }

    @AfterAll
    static void stopContainer() {
        mysql.stop();
    }

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
    }

    @Test
    @Order(1)
    void testSaveTask() throws Exception {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setCreatedAt(LocalDateTime.now().plusDays(3));
        taskDTO.setDescription("Integration Test");
        taskDTO.setDueDate(LocalDate.of(2024, 11, 12));
        taskDTO.setCompleted(false);
        taskDTO.setTitle("Test Task");

        mockMvc.perform(post("/api/v1/task/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200)) // Check response status code
                .andExpect(jsonPath("$.message").value("Success")) // Check success message
                .andExpect(jsonPath("$.data").isString());
    }

    @Test
    @Order(2)
    void testGetAllTasks() throws Exception {
        Task task = new Task();
        task.setCreatedAt(LocalDateTime.now().plusDays(3));
        task.setDescription("Integration Test");
        task.setDueDate(LocalDate.of(2024, 11, 12));
        task.setCompleted(true);
        task.setTitle("Test Task");
        taskRepository.saveAndFlush(task);

        mockMvc.perform(get("/api/v1/task/get-all-completed")
                        .param("status", "true")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].title").value("Test Task"))
                .andExpect(jsonPath("$.data[0].completed").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Success"));
    }

    @Test
    @Order(3)
    void testMarkAsDone() throws Exception {
        Task task = new Task();
        task.setCreatedAt(LocalDateTime.now().plusDays(3));
        task.setDescription("Integration Test");
        task.setDueDate(LocalDate.of(2024, 11, 12));
        task.setCompleted(false);
        task.setTitle("Test Task");
        task = taskRepository.save(task);

        mockMvc.perform(put("/api/v1/task/" + task.getTaskId() + "/complete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200)) // Check response status code
                .andExpect(jsonPath("$.message").value("Success")) // Check success message
                .andExpect(jsonPath("$.data").isString());

        Assertions.assertTrue(taskRepository.findById(task.getTaskId()).get().isCompleted());
    }
}
