package ie.gtludwig.pa.service.impl;

import ie.gtludwig.pa.dao.AxisJpaRepository;
import ie.gtludwig.pa.model.Axis;
import ie.gtludwig.pa.model.Project;
import ie.gtludwig.pa.model.Rule;
import ie.gtludwig.pa.service.AxisService;
import ie.gtludwig.pa.service.RuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service(value = "axisService")
public class AxisServiceImpl implements AxisService {

    private static Logger logger = LoggerFactory.getLogger(AxisServiceImpl.class);

    @Autowired
    private AxisJpaRepository axisJpaRepository;

    @Autowired
    private RuleService ruleService;

    @Override
    public Axis findByDescription(String description) {
        return axisJpaRepository.findByDescription(description);
    }

    @Override
    public Set<Axis> getDefaultAxisSet() {
        Set<Axis> axisSet = new HashSet<>();
        axisSet.add(findByDescription("Default Axis 0"));
        axisSet.add(findByDescription("Default Axis 1"));
        axisSet.add(findByDescription("Default Axis 2"));
        return axisSet;
    }

    @Override
    public Set<Axis> createDefaultAxisSetFromProject(Project project) {
        Set<Axis> axisSet = new HashSet<>();

        for (Axis axis : getDefaultAxisSet()) {
            Axis newAxis = axis;
            newAxis.setProject(project);
            newAxis.setRuleSet(ruleService.createRuleSetForAxis(axis));

            save(newAxis);
            axisSet.add(newAxis);
        }
        return axisSet;
    }

    @Override
    public Axis findById(String id) {
        return axisJpaRepository.getOne(id);
    }

    @Override
    public void save(Axis pojo) {
        logger.info("Saved axis for project {} with description {}.", pojo.getProject().getName(), pojo.getDescription());
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
        return axisJpaRepository.findAll();
    }
}
