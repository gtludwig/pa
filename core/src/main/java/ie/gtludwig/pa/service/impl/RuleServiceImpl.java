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

        String description = axis.getDescription();

        for (int i = 0; i < axis.getNumberOfRules(); i++) {
            rulesSet.add(ruleJpaRepository.saveAndFlush(new Rule(i, description + " " + i)));
        }

        return rulesSet;
    }

    @Override
    public Set<Rule> createDefaultRulesSetForAxis(Axis axis) {
         Set<Rule> rulesSet = new HashSet<>();

         String ruleDescription;

         for (int i = 0; i < axis.getNumberOfRules(); i++) {
             ruleDescription = axis.getDescription() + " " + i;
             if(findByDescription(ruleDescription) == null) {
                rulesSet.add(ruleJpaRepository.saveAndFlush(new Rule(0, ruleDescription)));
             } else {
                 rulesSet.add(findByDescription(ruleDescription));
             }
         }

        return rulesSet;
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
