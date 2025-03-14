package com.example.todoApp.todoAppBackend.repo;
import com.example.todoApp.todoAppBackend.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TaskRepo extends JpaRepository<Task, Integer> {
    Task getByTaskId(Integer id);
    Page<Task> findAllByisCompletedEquals(boolean b, Pageable pageable);
}
