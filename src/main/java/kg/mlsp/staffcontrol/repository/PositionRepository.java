package kg.mlsp.staffcontrol.repository;

import kg.mlsp.staffcontrol.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PositionRepository extends JpaRepository<Position, Integer>, JpaSpecificationExecutor<Position> {
}
