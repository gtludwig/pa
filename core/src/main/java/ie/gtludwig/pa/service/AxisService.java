package ie.gtludwig.pa.service;

import ie.gtludwig.pa.model.Axis;
import ie.gtludwig.pa.model.Project;
import ie.gtludwig.pa.service.generic.CrudService;

import java.util.List;
import java.util.Set;

public interface AxisService extends CrudService<Axis> {

    List<Axis> findAllByProject(Project project);

    Set<Axis> createDefaultAxisSetFromProject(Project project);
}
