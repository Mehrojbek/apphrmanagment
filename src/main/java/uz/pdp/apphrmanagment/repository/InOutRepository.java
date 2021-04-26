package uz.pdp.apphrmanagment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.apphrmanagment.entity.InOut;

import java.sql.Timestamp;
import java.util.UUID;
import java.util.*;

public interface InOutRepository extends JpaRepository<InOut, UUID> {
    @Query(value = "select * from in_out where user_id=:userId and time between :fromTime and :toTime;",nativeQuery = true)
    List<InOut> getAllInOutsByWorkerIdAndTime(UUID userId, Timestamp fromTime, Timestamp toTime);
}
