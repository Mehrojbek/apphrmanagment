package uz.pdp.apphrmanagment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.apphrmanagment.entity.User;

import java.util.Optional;
import java.util.UUID;
import java.util.*;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndEmailCode(String email, String emailCode);

    List<User> findAllByCreatedBy(UUID createdBy);
}
