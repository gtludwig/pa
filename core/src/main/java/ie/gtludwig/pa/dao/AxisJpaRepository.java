package ie.gtludwig.pa.dao;

import ie.gtludwig.pa.model.Axis;
import ie.gtludwig.pa.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AxisJpaRepository extends JpaRepository<Axis, String> {

//    List<Axis> findAllByProject(Project project);

    Axis findByDescription(String description);
}
