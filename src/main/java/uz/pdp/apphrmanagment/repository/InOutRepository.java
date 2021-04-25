package uz.pdp.apphrmanagment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.apphrmanagment.entity.InOut;

import java.util.UUID;

public interface InOutRepository extends JpaRepository<InOut, UUID> {
}
