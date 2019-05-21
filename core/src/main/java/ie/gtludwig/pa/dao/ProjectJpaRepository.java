package ie.gtludwig.pa.dao;

import ie.gtludwig.pa.model.Project;
import ie.gtludwig.pa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectJpaRepository extends JpaRepository<Project, String> {

    Project findByName(String name);

    List<Project> findAllByCreator(User creator);
}
