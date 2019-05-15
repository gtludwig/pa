package ie.gtludwig.pa.service.impl;

import ie.gtludwig.pa.dao.AxisElementJpaRepository;
import ie.gtludwig.pa.model.Axis;
import ie.gtludwig.pa.model.AxisElement;
import ie.gtludwig.pa.service.AxisElementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "axisElementService")
public class AxisElementServiceImpl implements AxisElementService {

    private static Logger logger = LoggerFactory.getLogger(AxisElementServiceImpl.class);

    @Autowired
    private AxisElementJpaRepository axisElementJpaRepository;

    @Override
    public List<AxisElement> findAllByAxis(Axis axis) {
        return axisElementJpaRepository.findAllByAxis(axis);
    }

    @Override
    public AxisElement findById(String id) {
        return axisElementJpaRepository.getOne(id);
    }

    @Override
    public void save(AxisElement pojo) {
        logger.info("Saved axis element with description: {}", pojo.getDescription());
        axisElementJpaRepository.save(pojo);
    }

    @Override
    public void remove(String id) {
        AxisElement axisElement = axisElementJpaRepository.getOne(id);
        logger.info("Removed axis element with description: {}", axisElement.getDescription());
        axisElementJpaRepository.delete(axisElement);
    }

    @Override
    public List<AxisElement> findAll() {
        return axisElementJpaRepository.findAll();
    }
}
