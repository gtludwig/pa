package ie.gtludwig.pa.service.impl;

import ie.gtludwig.pa.dao.RuleJpaRepository;
import ie.gtludwig.pa.model.Axis;
import ie.gtludwig.pa.model.Project;
import ie.gtludwig.pa.model.Rule;
import ie.gtludwig.pa.service.AxisService;
import ie.gtludwig.pa.service.RuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service(value = "ruleService")
public class RuleServiceImpl implements RuleService {

    private static Logger logger = LoggerFactory.getLogger(RuleServiceImpl.class);

    @Autowired
    private RuleJpaRepository ruleJpaRepository;

    @Autowired
    private AxisService axisService;

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
    public List<Rule> findAllFromAxisByAxisId(String axisId) {
        return axisService.findById(axisId).getRuleSet().stream()
                .sorted(Comparator.comparingInt(Rule::getOrdering))
                .collect(Collectors.toList());
    }

    @Override
    public void createRule(String axisId, String description) {
        Axis axis = findAxisByAxisId(axisId);

        Rule rule = new Rule();
        rule.setOrdering(axis.getRuleSet().size());
        rule.setDescription(description);

        rule = ruleJpaRepository.saveAndFlush(rule);

        updateAxisFromRule(axis, rule);
    }

    private void updateAxisFromRule(Axis axis, Rule rule) {
        axis.getRuleSet().add(rule);
        axis.setNumberOfRules(axis.getRuleSet().size());
        axisService.save(axis);
    }

    @Override
    public void updateRule(String ruleId, String description) {
        Rule rule = findById(ruleId);
        rule.setDescription(description);
        save(rule);
    }

    @Override
    public Axis findAxisByAxisId(String axisId) {
        return axisService.findById(axisId);
    }

    @Override
    public Axis findAxisFromRule(Rule rule) {
        return axisService.findAll().stream().filter(axis -> axis.getRuleSet().contains(rule)).findFirst().orElse(null);
    }

    @Override
    public Project findProjectByAxisId(String axisId) {
        return axisService.findProjectFromAxisId(axisId);
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
