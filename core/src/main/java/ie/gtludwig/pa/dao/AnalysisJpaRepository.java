package ie.gtludwig.pa.dao;

import ie.gtludwig.pa.model.Analysis;
import ie.gtludwig.pa.model.Project;
import ie.gtludwig.pa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnalysisJpaRepository extends JpaRepository<Analysis, String> {

    List<Analysis> findAllByAnalyst(User analyst);

    List<Analysis> findAllByProject(Project project);

}
