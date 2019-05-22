package ie.gtludwig.pa.service.impl;

import ie.gtludwig.pa.dao.AxisJpaRepository;
import ie.gtludwig.pa.model.Axis;
import ie.gtludwig.pa.model.Project;
import ie.gtludwig.pa.service.AxisService;
import ie.gtludwig.pa.service.ProjectService;
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

    protected Axis getDefaultAxis(int ordering, String axisDescription) {

        Axis axis;

        if (findByDescription(axisDescription) == null) {
            axis = new Axis();
            axis.setOrdering(ordering);
            axis.setDescription(axisDescription);
            axis.setGuideline(false);

            axis = axisJpaRepository.saveAndFlush(axis);
            axis.setRuleSet(ruleService.findDefaultRulesSetForAxis(axis));
        } else {
            axis = findByDescription(axisDescription);
        }

        return axis;
    }

    @Override
    public Set<Axis> createDefaultAxisSetFromProject(Project project) {
        Set<Axis> axisSet = new HashSet<>();

        for (int i = 0; i < 3; i++) {
            axisSet.add(getDefaultAxis(i, "Default Axis " + i));
        }
        return axisSet;
    }

    @Override
    public Set<Axis> findAllByProjectId(String projectId) {
        Set<Axis> projectAxis = new HashSet<>();
        Project project = projectService.findById(projectId);
        projectAxis.add(project.getGuidelineAxis());
        for (Axis axis : project.getAxisSet()) {
            projectAxis.add(axis);
        }
        return projectAxis;
    }

    @Override
    public Axis findDefaultGuidelineAxis() {
        String defaultGuidelineAxis = "Default Guideline Axis";
        Axis guidelineAxis = findByDescription(defaultGuidelineAxis);
        if(guidelineAxis == null) {
            guidelineAxis = new Axis();
            guidelineAxis.setOrdering(0);
            guidelineAxis.setDescription(defaultGuidelineAxis);
            guidelineAxis.setGuideline(true);

            guidelineAxis = axisJpaRepository.saveAndFlush(guidelineAxis);
            guidelineAxis.setRuleSet(ruleService.findDefaultRulesSetForAxis(guidelineAxis));


            return guidelineAxis;
        }

        return guidelineAxis;
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
        logger.info("Removed axis with id: {}", axis.getId());
        axisJpaRepository.delete(axis);
    }

    @Override
    public List<Axis> findAll() {
        List<Axis> sorted = axisJpaRepository.findAll()
                .stream()
                .sorted(Comparator.comparingInt(Axis::getOrdering))
                .sorted(Comparator.comparing(Axis::isGuideline).reversed())
                .collect(Collectors.toList());
        return sorted;
    }
}
