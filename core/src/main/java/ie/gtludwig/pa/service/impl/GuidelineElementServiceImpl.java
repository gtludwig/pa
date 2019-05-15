package ie.gtludwig.pa.service.impl;

import ie.gtludwig.pa.dao.GuidelineElementJpaRepository;
import ie.gtludwig.pa.model.Guideline;
import ie.gtludwig.pa.model.GuidelineElement;
import ie.gtludwig.pa.service.GuidelineElementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "guidelineElementService")
public class GuidelineElementServiceImpl implements GuidelineElementService {

    private static Logger logger = LoggerFactory.getLogger(GuidelineElementServiceImpl.class);

    @Autowired
    private GuidelineElementJpaRepository guidelineElementJpaRepository;

    @Override
    public List<GuidelineElement> findAllByGuideline(Guideline guideline) {
        return guidelineElementJpaRepository.findAllByGuideline(guideline);
    }

    @Override
    public GuidelineElement findById(String id) {
        return guidelineElementJpaRepository.getOne(id);
    }

    @Override
    public void save(GuidelineElement pojo) {
        logger.info("Saved guideline element with description: " + pojo.getDescription());
        guidelineElementJpaRepository.save(pojo);
    }

    @Override
    public void remove(String id) {
        GuidelineElement guidelineElement = guidelineElementJpaRepository.getOne(id);
        logger.info("Removed guideline element with description: " + guidelineElement.getDescription());
    }

    @Override
    public List<GuidelineElement> findAll() {
        return guidelineElementJpaRepository.findAll();
    }
}
