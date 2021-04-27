package uz.pdp.apphrmanagment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.apphrmanagment.entity.Month;

public interface MonthRepository extends JpaRepository<Month,Integer> {
}
