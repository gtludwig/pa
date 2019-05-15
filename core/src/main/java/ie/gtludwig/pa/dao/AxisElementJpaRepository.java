package ie.gtludwig.pa.dao;

import ie.gtludwig.pa.model.Axis;
import ie.gtludwig.pa.model.AxisElement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AxisElementJpaRepository extends JpaRepository<AxisElement, String> {

    List<AxisElement> findAllByAxis(Axis axis);
}
