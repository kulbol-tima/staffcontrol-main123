package kg.mlsp.staffcontrol.repository;

import kg.mlsp.staffcontrol.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffRepository extends JpaRepository<Staff, Integer> {
}