package uz.pdp.apphrmanagment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.apphrmanagment.entity.Role;
import uz.pdp.apphrmanagment.entity.enums.RoleName;

public interface RoleRepository extends JpaRepository<Role,Integer> {
    Role getByName(RoleName name);
}
