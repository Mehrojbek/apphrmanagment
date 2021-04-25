package uz.pdp.apphrmanagment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.apphrmanagment.entity.Task;

import java.util.Optional;
import java.util.UUID;
import java.util.*;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    Optional<Task> findByIdAndPerformerId(UUID id, UUID performer_id);
    List<Task> findAllByPerformerId(UUID performer_id);
    List<Task> findAllByCreatedBy(UUID createdBy);
}
