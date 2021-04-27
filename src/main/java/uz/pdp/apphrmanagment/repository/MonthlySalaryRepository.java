package uz.pdp.apphrmanagment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.apphrmanagment.entity.MonthlySalary;

import java.util.List;
import java.util.UUID;

public interface MonthlySalaryRepository extends JpaRepository<MonthlySalary, UUID> {

    List<MonthlySalary> findAllByWorkerIdAndMonth_Id(UUID worker_id, Integer month_id);
    List<MonthlySalary> findAllByMonth_Id(Integer month_id);
}
