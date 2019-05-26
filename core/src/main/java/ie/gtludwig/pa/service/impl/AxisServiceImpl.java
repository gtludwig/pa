package ie.gtludwig.pa.service.impl;

import ie.gtludwig.pa.dao.AxisJpaRepository;
import ie.gtludwig.pa.model.Axis;
import ie.gtludwig.pa.model.Project;
import ie.gtludwig.pa.model.Rule;
import ie.gtludwig.pa.service.AxisService;
import ie.gtludwig.pa.service.ProjectService;
import ie.gtludwig.pa.service.RuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service(value = "axisService")
public class AxisServiceImpl implements AxisService {

    private static Logger logger = LoggerFactory.getLogger(AxisServiceImpl.class);

    @Autowired
    private AxisJpaRepository axisJpaRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private RuleService ruleService;

    @Override
    public Axis findByDescription(String description) {
        return axisJpaRepository.findByDescription(description);
    }

    protected Axis findDefaultGuidelineAxis() {
        String defaultGuidelineAxis = "Default Guideline Axis";
        Axis guidelineAxis = findByDescription(defaultGuidelineAxis);
        if(guidelineAxis == null) {
            guidelineAxis = new Axis();
            guidelineAxis.setOrdering(0);
            guidelineAxis.setDescription(defaultGuidelineAxis);
            guidelineAxis.setApplicationDefault(true);
            guidelineAxis.setGuideline(true);

            guidelineAxis = axisJpaRepository.saveAndFlush(guidelineAxis);
        }

        return guidelineAxis;
    }

    @Override
    public Axis createGuidelineAxisForProject(Project project) {
        Axis defaultGuidelineAxis = findDefaultGuidelineAxis();

        Axis projectGuidelineAxis = new Axis();
        projectGuidelineAxis.setOrdering(defaultGuidelineAxis.getOrdering());
        projectGuidelineAxis.setDescription("Guideline for project " + project.getName());
        projectGuidelineAxis.setApplicationDefault(false);
        projectGuidelineAxis.setGuideline(true);

        projectGuidelineAxis = axisJpaRepository.saveAndFlush(projectGuidelineAxis);
        projectGuidelineAxis.setRuleSet(ruleService.createDefaultRulesSetForAxis(projectGuidelineAxis));

        return projectGuidelineAxis;
    }


    protected Axis getDefaultAxis(int ordering, String axisDescription) {

        Axis axis = findByDescription(axisDescription);

        if (axis == null) {
            axis = new Axis();
            axis.setOrdering(ordering);
            axis.setDescription(axisDescription);
            axis.setApplicationDefault(true);
            axis.setGuideline(false);

            axis = axisJpaRepository.saveAndFlush(axis);
        }

        return axis;
    }

    @Override
    public Set<Axis> createAxisSetForProject(Project project) {
        Set<Axis> axisSet = new HashSet<>();

//        for (int i = 0; i < findAllDefaultAxis().size(); i++) {
        for (int i = 0; i < 3; i++) {
            Axis defaultAxis = getDefaultAxis(i, "Default Axis " + i);

            Axis projectAxis = new Axis();

            projectAxis.setOrdering(defaultAxis.getOrdering());
            projectAxis.setDescription("Project " + project.getName() + " :: Axis " + i);
            projectAxis.setApplicationDefault(!defaultAxis.isApplicationDefault());
            projectAxis.setGuideline(defaultAxis.isGuideline());

            projectAxis = axisJpaRepository.saveAndFlush(projectAxis);
            projectAxis.setRuleSet(ruleService.createDefaultRulesSetForAxis(projectAxis));

            axisSet.add(projectAxis);
        }
        return axisSet;
    }

    @Override
    public void addRuleToAxis(String axisId) {
        Axis axis = axisJpaRepository.getOne(axisId);
        int newRuleOrdering = axis.getRuleSet().size();
        axis.getRuleSet().add(new Rule(newRuleOrdering, "New Rule for Axis: " + axis.getDescription()));
        save(axis);
    }

    @Override
    public void removeRuleFromAxis(String axisId) {
        Axis axis = axisJpaRepository.getOne(axisId);
        Set<Rule> rulesSet = axis.getRuleSet();
        Optional<Rule> matchingObject = rulesSet.stream().filter(rule -> rule.getOrdering() == rulesSet.size()).findFirst();
        rulesSet.remove(matchingObject.get());
        save(axis);
    }

    @Override
    public List<Axis> findAll(boolean guideline) {
        return axisJpaRepository.findAllByGuideline(guideline);
    }

    @Override
    public Axis findById(String id) {
        return axisJpaRepository.getOne(id);
    }

    @Override
    public void save(Axis pojo) {
        logger.info("Saved axis with description {}.", pojo.getDescription());
        axisJpaRepository.save(pojo);
    }

    @Override
    public void remove(String id) {
        Axis axis = axisJpaRepository.getOne(id);

        Project project = findProjectFromAxis(axis);
        if (project != null) {
            project.getAxisSet().remove(axis);
            projectService.save(project);
        }

        logger.info("Removed axis with id: {}", axis.getId());
        axisJpaRepository.delete(axis);
    }

    @Override
    public List<Axis> findAll() {
        return axisJpaRepository.findAll();
    }

    @Override
    public List<Axis> findAllDefaultAxis() {
        return findAll()
                .stream()
                .sorted(Comparator.comparingInt(Axis::getOrdering))
                .sorted(Comparator.comparing(Axis::isGuideline).reversed())
                .filter(Axis::isApplicationDefault)
                .collect(Collectors.toList());
    }

        @Override
    public List<Axis> findAllByProject(String projectId) {
        Project project = projectService.findById(projectId);
        List<Axis> projectAxisList = new ArrayList<>();
        projectAxisList.add(project.getGuidelineAxis());
        for (Axis axis : project.getAxisSet()) {
            projectAxisList.add(axis);
        }
        return projectAxisList.stream()
                .sorted(Comparator.comparingInt(Axis::getOrdering))
                .sorted(Comparator.comparing(Axis::isGuideline).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public void createAxis(String projectId, String description, boolean applicationDefault, boolean guideline, int numberOfRules) {

        Axis axis = new Axis();
        axis.setApplicationDefault(applicationDefault);
        axis.setGuideline(guideline);
        axis.setDescription(description);
        axis.setNumberOfRules(numberOfRules);


        if (applicationDefault) {
            axis.setOrdering(findAllDefaultAxis().size());
        }
        if (!applicationDefault){
            Project project = projectService.findById(projectId);
            axis.setOrdering(project.getAxisSet().size());
            project.getAxisSet().add(axis);
//            if (rule) {
//                project.setGuidelineAxis(axis);
//            }
            projectService.save(project);
        }
        axis = axisJpaRepository.saveAndFlush(axis);

        updateRuleSet(axis);
    }

    protected void updateRuleSet(Axis axis) {
        Set<Rule> ruleSet = ruleService.createRuleSetForAxis(axis);

        axis.setRuleSet(ruleSet);
        save(axis);

    }

    @Override
    public void updateAxis(String axisId, String projectId, String description, boolean applicationDefault, boolean guideline, int numberOfRules) {
        Axis axis = findById(axisId);

//        axis.setOrdering(ordering);
        axis.setDescription(description);
        axis.setApplicationDefault(applicationDefault);
        axis.setGuideline(guideline);
        axis.setNumberOfRules(numberOfRules);


        axis = axisJpaRepository.saveAndFlush(axis);
        updateRuleSet(axis);

        if (projectId != null) {
            Project project = findProjectByProjectId(projectId);
            project.getAxisSet().add(axis);
            projectService.save(project);
        }
    }

    @Override
    public Project findProjectByProjectId(String projectId) {
        return projectService.findById(projectId);
    }

    @Override
    public Project findProjectFromAxisId(String axisId) {
        return findProjectFromAxis(findById(axisId));
    }

    @Override
    public Project findProjectFromAxis(Axis axis) {
        return projectService.findByGuidelineAxisEqualsOrAxisSet(axis);
    }
}
