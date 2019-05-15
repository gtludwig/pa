package ie.gtludwig.pa.service;

import ie.gtludwig.pa.model.Project;
import ie.gtludwig.pa.model.User;
import ie.gtludwig.pa.service.generic.CrudService;

import java.util.List;

public interface ProjectService extends CrudService<Project> {

    void updateProject(Project project);

    List<Project> findAllByCreator(User creator);

}
