package ie.gtludwig.pa.service;

import ie.gtludwig.pa.model.Guideline;
import ie.gtludwig.pa.service.generic.CrudService;

public interface GuidelineService extends CrudService<Guideline> {

    Guideline findByDescription(String description);

    Guideline createGuideline(String name, String description, int numberOfRules);

    Guideline updateGuideline(String id, String name, String description, int numberOfRules);

//    List<Guideline> findAllByProject(Project project);
}
