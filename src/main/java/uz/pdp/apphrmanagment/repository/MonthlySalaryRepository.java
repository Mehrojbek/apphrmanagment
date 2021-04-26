package uz.pdp.apphrmanagment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.apphrmanagment.entity.MonthlySalary;

import java.util.UUID;

public interface MonthlySalaryRepository extends JpaRepository<MonthlySalary, UUID> {
}
