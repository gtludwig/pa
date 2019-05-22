package ie.gtludwig.pa.service.impl;

import ie.gtludwig.pa.dao.RuleJpaRepository;
import ie.gtludwig.pa.model.Axis;
import ie.gtludwig.pa.model.Rule;
import ie.gtludwig.pa.service.RuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service(value = "ruleService")
public class RuleServiceImpl implements RuleService {

    private static Logger logger = LoggerFactory.getLogger(RuleServiceImpl.class);

    @Autowired
    private RuleJpaRepository ruleJpaRepository;

    @Override
    public Rule findByDescription(String description) {
        return ruleJpaRepository.findByDescription(description);
    }

    @Override
    public Set<Rule> createRuleSetForAxis(Axis axis) {
        Set<Rule> rulesSet = new HashSet<>();

        for (Rule rule : axis.getRuleSet()) {
            rulesSet.add(ruleJpaRepository.saveAndFlush(new Rule(rule.getOrdering(), rule.getDescription())));
        }

        return rulesSet;
    }

    @Override
    public Set<Rule> findDefaultRulesSetForAxis(Axis axis) {
         Set<Rule> rulesSet = new HashSet<>();

         String ruleDescription;

         for (int i = 0; i < 3; i++) {
             ruleDescription = axis.getDescription() + i;
             if(findByDescription(ruleDescription) == null) {
                rulesSet.add(ruleJpaRepository.saveAndFlush(new Rule(0, ruleDescription)));
             } else {
                 rulesSet.add(findByDescription(ruleDescription));
             }
         }

        return rulesSet;
    }



    @Override
    public Set<Rule> createRulesSetForGuideline(String name, int numberOfRules) {
        Set<Rule> rulesSet = new HashSet<>();

        for (int i = 0; i < numberOfRules; i++) {
            rulesSet.add(new Rule(i, "Guideline " + name + " rule " + i));
        }

        return rulesSet;
    }

    @Override
    public Set<Rule> updateRulesSetForGuideline_addRules(Set<Rule> rulesSet, String name, int numberOfRules) {
        Set<Rule> newRulesSet = rulesSet;

        int lastRuleIndex = rulesSet.size();
        int additionalRules = numberOfRules - lastRuleIndex;

        for (int i  = rulesSet.size(); i < additionalRules; i++) {
            newRulesSet.add(new Rule(i, "Guideline " + name + " rule " + i));
        }

        return newRulesSet;
    }

    @Override
    public Rule findById(String id) {
        return ruleJpaRepository.getOne(id);
    }

    @Override
    public void save(Rule pojo) {
        logger.info("Saved rule with description {}.", pojo.getDescription());
        ruleJpaRepository.save(pojo);
    }

    @Override
    public void remove(String id) {
        Rule rule = ruleJpaRepository.getOne(id);
        logger.info("Removed rule with id: {}", rule.getId());
        ruleJpaRepository.delete(rule);
    }

    @Override
    public List<Rule> findAll() {
        return ruleJpaRepository.findAll();
    }
}
