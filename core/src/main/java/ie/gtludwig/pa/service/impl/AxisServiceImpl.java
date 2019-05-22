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

        for (int i = 0; i < 3; i++) {
            Axis defaultAxis = getDefaultAxis(i, "Default Axis " + i);

            Axis projectAxis = new Axis();

            projectAxis.setOrdering(defaultAxis.getOrdering());
            projectAxis.setDescription("Project " + project.getName() + " :: Axis " + i);
            projectAxis.setApplicationDefault(!defaultAxis.isApplicationDefault());
            projectAxis.setGuideline(defaultAxis.isGuideline());

            projectAxis = axisJpaRepository.saveAndFlush(projectAxis);
            projectAxis.setRulesSet(ruleService.findDefaultRulesSetForAxis(projectAxis));

            axisSet.add(projectAxis);
        }
        return axisSet;
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
        projectGuidelineAxis.setRulesSet(ruleService.findDefaultRulesSetForAxis(projectGuidelineAxis));

        return projectGuidelineAxis;
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
        List<Axis> sorted = axisJpaRepository.findAllByApplicationDefaultIsFalse()
                .stream()
                .sorted(Comparator.comparingInt(Axis::getOrdering))
                .sorted(Comparator.comparing(Axis::isGuideline).reversed())
                .collect(Collectors.toList());
        return sorted;
    }
}
