package ie.gtludwig.pa.service.impl;

import ie.gtludwig.pa.dao.AxisJpaRepository;
import ie.gtludwig.pa.model.Axis;
import ie.gtludwig.pa.model.Project;
import ie.gtludwig.pa.service.AxisElementService;
import ie.gtludwig.pa.service.AxisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service(value = "axisService")
public class AxisServiceImpl implements AxisService {

    private static Logger logger = LoggerFactory.getLogger(AnalysisServiceImpl.class);

    @Autowired
    private AxisJpaRepository axisJpaRepository;

    @Autowired
    private AxisElementService axisElementService;

    @Override
    public List<Axis> findAllByProject(Project project) {
        return axisJpaRepository.findAllByProject(project);
    }

    @Override
    public Set<Axis> createDefaultAxisSetFromProject(Project project) {
        Set<Axis> axisSet = new HashSet<>();

        // Create Axis 1
        Axis axis1 = new Axis();
        axis1.setOrder(1);
        axis1.setDescription("Default axis 1 for project: " + project.getDescription());
        axis1.setActive(true);
        axis1.setProject(project);

        // Create Axis 2
        Axis axis2 = new Axis();
        axis2.setOrder(2);
        axis2.setDescription("Default axis 2 for project: " + project.getDescription());
        axis2.setActive(true);
        axis2.setProject(project);

        // Create Axis 3
        Axis axis3 = new Axis();
        axis3.setOrder(3);
        axis3.setDescription("Default axis 3 for project: " + project.getDescription());
        axis3.setActive(true);
        axis3.setProject(project);

        axisSet.add(axis1);
        axisSet.add(axis2);
        axisSet.add(axis3);

        save(axis1);
        save(axis2);
        save(axis3);

        createDefaulAxisElementSetFromAxis(axis1);
        createDefaulAxisElementSetFromAxis(axis2);
        createDefaulAxisElementSetFromAxis(axis3);

        return axisSet;
    }

    protected void createDefaulAxisElementSetFromAxis(Axis axis) {
        axisElementService.createDefaulAxisElementSetFromAxis(axis);
    }

    @Override
    public Axis findById(String id) {
        return axisJpaRepository.getOne(id);
    }

    @Override
    public void save(Axis pojo) {
        logger.info("Saved axis with description: {}", pojo.getDescription());
        axisJpaRepository.save(pojo);
    }

    @Override
    public void remove(String id) {
        Axis axis = axisJpaRepository.getOne(id);
        logger.info("Removed axis with description: {}", axis.getDescription());
        axisJpaRepository.delete(axis);
    }

    @Override
    public List<Axis> findAll() {
        return axisJpaRepository.findAll();
    }
}
