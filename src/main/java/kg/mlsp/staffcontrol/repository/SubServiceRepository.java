package kg.mlsp.staffcontrol.repository;

import kg.mlsp.staffcontrol.model.SubService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SubServiceRepository extends JpaRepository<SubService, Integer>, JpaSpecificationExecutor<SubService> {
}