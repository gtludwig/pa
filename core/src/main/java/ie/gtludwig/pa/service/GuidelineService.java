package ie.gtludwig.pa.service;

import ie.gtludwig.pa.model.Guideline;
import ie.gtludwig.pa.model.Project;
import ie.gtludwig.pa.service.generic.CrudService;

import java.util.List;

public interface GuidelineService extends CrudService<Guideline> {

    List<Guideline> findAllByProject(Project project);
}
