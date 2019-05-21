package ie.gtludwig.pa.service.impl;

import ie.gtludwig.pa.dao.GuidelineJpaRepository;
import ie.gtludwig.pa.model.Guideline;
import ie.gtludwig.pa.service.GuidelineService;
import ie.gtludwig.pa.service.RuleService;
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
    private RuleService ruleService;

//    @Autowired
//    private GuidelineElementService guidelineElementService;

    @Override
    public Guideline findByDescription(String description) {
        return guidelineJpaRepository.findByDescription(description);
    }

//    @Override
//    public List<Guideline> findAllByProject(Project project) {
//        return guidelineJpaRepository.findAllByProject(project);
//    }


    @Override
    public Guideline createGuideline(String name, String description, int numberofRules) {
        Guideline guideline = new Guideline();
        guideline.setName(name);
        guideline.setDescription(description);
        guideline.setOrdering(0);
        guideline.setRulesSet(ruleService.createRulesSetForGuideline(name, numberofRules));

        try {
            save(guideline);
            logger.info("Guideline with name <<{}>> successfully updated.", name);
        } catch (Exception e) {
            logger.error("Unable to create guideline with name <<{}>>.", name);
        }

        return guideline;
    }

    @Override
    public Guideline updateGuideline(String id, String name, String description, int numberOfRules) {
        Guideline guideline = findById(id);
        guideline.setName(name);
        guideline.setDescription(description);
        guideline.setOrdering(0);
        if (numberOfRules > guideline.getRulesSet().size()) {

        }
        return null;
    }

    @Override
    public Guideline findById(String id) {
        return guidelineJpaRepository.getOne(id);
    }

    @Override
    public void save(Guideline pojo) {
//        logger.info("Saved guideline with description: {} for project: {}", pojo.getDescription(), pojo.getProject().getDescription());
        logger.info("Saved guideline with description: {}.", pojo.getDescription());
        guidelineJpaRepository.save(pojo);

//        createDefaultGuidelineElement(pojo);
    }

    protected void createDefaultGuidelineElement(Guideline guideline) {
//        GuidelineElement guidelineElement = new GuidelineElement();
//        guidelineElement.setOrder(1);
//        guidelineElement.setDescription("Default guideline element for guideline: " + guideline.getDescription());
//        guidelineElementService.save(guidelineElement);
    }

    @Override
    public void remove(String id) {
        Guideline guideline = guidelineJpaRepository.getOne(id);
//        logger.info("Removed guideline with description: {} for project: {}", guideline.getDescription(), guideline.getProject().getDescription());
        logger.info("Removed guideline with description: {}.", guideline.getDescription());
        guidelineJpaRepository.delete(guideline);
    }

    @Override
    public List<Guideline> findAll() {
        return guidelineJpaRepository.findAll();
    }
}
