package ie.gtludwig.pa.dao;

import ie.gtludwig.pa.model.Guideline;
import ie.gtludwig.pa.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GuidelineJpaRepository extends JpaRepository<Guideline, String> {

    List<Guideline> findAllByProject(Project project);
}
