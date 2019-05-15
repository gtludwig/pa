package ie.gtludwig.pa.service.impl;

import ie.gtludwig.pa.dao.AxisJpaRepository;
import ie.gtludwig.pa.model.Axis;
import ie.gtludwig.pa.model.Project;
import ie.gtludwig.pa.service.AxisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "axisService")
public class AxisServiceImpl implements AxisService {

    private static Logger logger = LoggerFactory.getLogger(AnalysisServiceImpl.class);

    @Autowired
    private AxisJpaRepository axisJpaRepository;

    @Override
    public List<Axis> findAllByProject(Project project) {
        return axisJpaRepository.findAllByProject(project);
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
