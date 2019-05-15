package ie.gtludwig.pa.service;

import ie.gtludwig.pa.model.Axis;
import ie.gtludwig.pa.model.Project;
import ie.gtludwig.pa.service.generic.CrudService;

import java.util.List;

public interface AxisService extends CrudService<Axis> {

    List<Axis> findAllByProject(Project project);
}
