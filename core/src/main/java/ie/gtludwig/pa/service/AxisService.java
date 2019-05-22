package ie.gtludwig.pa.service;

import ie.gtludwig.pa.model.Axis;
import ie.gtludwig.pa.model.Project;
import ie.gtludwig.pa.service.generic.CrudService;

import java.util.List;
import java.util.Set;

public interface AxisService extends CrudService<Axis> {

    Set<Axis> findAllByProjectId(String projectId);

    List<Axis> findAll(boolean guideline);

    Axis findByDescription(String description);

//    Axis findDefaultGuidelineAxis();

    Axis createGuidelineAxisForProject(Project project);

    Set<Axis> createAxisSetForProject(Project project);
}
