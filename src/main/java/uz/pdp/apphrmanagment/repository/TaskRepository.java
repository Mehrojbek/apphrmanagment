package uz.pdp.apphrmanagment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.apphrmanagment.entity.Task;
import uz.pdp.apphrmanagment.entity.enums.TaskStatus;

import java.sql.Timestamp;

import java.util.UUID;
import java.util.*;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findAllByPerformerId(UUID performer_id);
    List<Task> findAllByCreatedBy(UUID createdBy);

    @Query(value = "select * from task where performer_id=:userId and status='STATUS_DONE' and created_at " +
            "between :fromTime and :toTime",nativeQuery = true)
    List<Task> getAllTasksByWorkerIdAndTime(UUID userId, Timestamp fromTime, Timestamp toTime);

    List<Task> findAllByStatus(TaskStatus status);

    List<Task> findAllByCreatedByAndStatus(UUID createdBy, TaskStatus status);
}
