package ie.gtludwig.pa.service.impl;

import ie.gtludwig.pa.dao.GuidelineJpaRepository;
import ie.gtludwig.pa.model.Guideline;
import ie.gtludwig.pa.model.GuidelineElement;
import ie.gtludwig.pa.model.Project;
import ie.gtludwig.pa.service.GuidelineElementService;
import ie.gtludwig.pa.service.GuidelineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "guidelineService")
public class GuidelineServiceImpl implements GuidelineService {

    private static Logger logger = LoggerFactory.getLogger(GuidelineServiceImpl.class);

    @Autowired
    private GuidelineJpaRepository guidelineJpaRepository;

    @Autowired
    private GuidelineElementService guidelineElementService;


    @Override
    public List<Guideline> findAllByProject(Project project) {
        return guidelineJpaRepository.findAllByProject(project);
    }

    @Override
    public Guideline findById(String id) {
        return guidelineJpaRepository.getOne(id);
    }

    @Override
    public void save(Guideline pojo) {
        logger.info("Saved guideline with description: {} for project: {}", pojo.getDescription(), pojo.getProject().getDescription());
        guidelineJpaRepository.save(pojo);

        createDefaultGuidelineElement(pojo);
    }

    protected void createDefaultGuidelineElement(Guideline guideline) {
        GuidelineElement guidelineElement = new GuidelineElement();
        guidelineElement.setOrder(1);
        guidelineElement.setDescription("Default guideline element for guideline: " + guideline.getDescription());
        guidelineElement.setGuideline(guideline);
        guidelineElementService.save(guidelineElement);
    }

    @Override
    public void remove(String id) {
        Guideline guideline = guidelineJpaRepository.getOne(id);
        logger.info("Removed guideline with description: {} for project: {}", guideline.getDescription(), guideline.getProject().getDescription());
        guidelineJpaRepository.delete(guideline);
    }

    @Override
    public List<Guideline> findAll() {
        return guidelineJpaRepository.findAll();
    }
}