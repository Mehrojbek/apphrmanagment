package uz.pdp.apphrmanagment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.apphrmanagment.entity.Task;

import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
}
