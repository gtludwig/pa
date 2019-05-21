package ie.gtludwig.pa.service;

import ie.gtludwig.pa.model.Axis;
import ie.gtludwig.pa.model.Rule;
import ie.gtludwig.pa.service.generic.CrudService;

import java.util.Set;

public interface RuleService extends CrudService<Rule> {

    Rule findByDescription(String description);

    Set<Rule> createRuleSetForAxis(Axis axis);

    Set<Rule> createRulesSetForGuideline(String name, int numberOfRules);

    Set<Rule> updateRulesSetForGuideline_addRules(Set<Rule> rulesSet, String name, int numberOfRules);


}
