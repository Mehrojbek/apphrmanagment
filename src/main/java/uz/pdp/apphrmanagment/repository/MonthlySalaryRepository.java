package uz.pdp.apphrmanagment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.apphrmanagment.entity.MonthlySalary;
import uz.pdp.apphrmanagment.entity.enums.MonthName;

import java.util.List;
import java.util.UUID;

public interface MonthlySalaryRepository extends JpaRepository<MonthlySalary, UUID> {

    List<MonthlySalary> findAllByWorkerIdAndMonthName(UUID worker_id, MonthName monthName);
    List<MonthlySalary> findAllByMonthName(MonthName monthName);
}
