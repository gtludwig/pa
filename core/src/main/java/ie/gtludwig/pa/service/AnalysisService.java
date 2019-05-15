package ie.gtludwig.pa.service;

import ie.gtludwig.pa.model.Analysis;
import ie.gtludwig.pa.model.Project;
import ie.gtludwig.pa.model.User;
import ie.gtludwig.pa.service.generic.CrudService;

import java.util.List;

public interface AnalysisService extends CrudService<Analysis> {

    List<Analysis> findAllByAnalyst(User analyst);

    List<Analysis> findAllByProject(Project project);
}
