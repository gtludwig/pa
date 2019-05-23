package ie.gtludwig.pa.service;

import ie.gtludwig.pa.model.Axis;
import ie.gtludwig.pa.model.Project;
import ie.gtludwig.pa.service.generic.CrudService;

import java.util.List;
import java.util.Set;

public interface AxisService extends CrudService<Axis> {

    List<Axis> findAll(boolean guideline);

    List<Axis> findAllDefaultAxis();

    Axis findByDescription(String description);

    Axis createGuidelineAxisForProject(Project project);

    Set<Axis> createAxisSetForProject(Project project);

    void addRuleToAxis(String axisId);

    void removeRuleFromAxis(String axisId);

    List<Axis> findAllByProject(String projectId);

    void createAxis(String projectId, String description, boolean applicationDefault, boolean guideline, int numberOfRules);

    void updateAxis(String axisId, String projectId, String description, boolean applicationDefault, boolean guideline, int numberOfRules);

    Project findProjectByProjectId(String projectId);

    Project findProjectFromAxis(Axis axis);

}
