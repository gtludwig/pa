package ie.gtludwig.pa.service;

import ie.gtludwig.pa.model.Guideline;
import ie.gtludwig.pa.model.GuidelineElement;
import ie.gtludwig.pa.service.generic.CrudService;

import java.util.List;

public interface GuidelineElementService extends CrudService<GuidelineElement> {

    List<GuidelineElement> findAllByGuideline(Guideline guideline);
}
