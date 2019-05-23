package ie.gtludwig.pa.service;

import ie.gtludwig.pa.model.Axis;
import ie.gtludwig.pa.model.Rule;
import ie.gtludwig.pa.service.generic.CrudService;

import java.util.Set;

public interface RuleService extends CrudService<Rule> {

    Set<Rule> createRuleSetForAxis(Axis axis);

    Set<Rule> createDefaultRulesSetForAxis(Axis axis);

    Rule findByDescription(String description);
}
