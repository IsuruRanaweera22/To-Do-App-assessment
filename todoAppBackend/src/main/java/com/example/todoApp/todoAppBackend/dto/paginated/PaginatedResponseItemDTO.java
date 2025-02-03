package com.example.todoApp.todoAppBackend.dto.paginated;
import com.example.todoApp.todoAppBackend.dto.TaskDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaginatedResponseItemDTO {
    List<TaskDTO> itemsByState;
    private long dataCount;
}
