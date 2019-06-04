package ie.gtludwig.pa.service.impl;

import ie.gtludwig.pa.dao.AnalysisJpaRepository;
import ie.gtludwig.pa.model.Analysis;
import ie.gtludwig.pa.model.Project;
import ie.gtludwig.pa.model.User;
import ie.gtludwig.pa.service.AnalysisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service(value = "analysisService")
public class AnalysisServiceImpl implements AnalysisService {

    private static Logger logger = LoggerFactory.getLogger(AnalysisServiceImpl.class);

    @Autowired
    private AnalysisJpaRepository analysisJpaRepository;

    @Override
    public List<Analysis> findAllByAnalyst(User analyst) {
        return analysisJpaRepository.findAllByAnalyst(analyst);
    }

    @Override
    public Analysis findById(String id) {
        return analysisJpaRepository.getOne(id);
    }

    @Override
    public void save(Analysis pojo) {
        logger.info("Saved analysis by user with email {} on {} with result of {}", pojo.getAnalyst().getEmail(), pojo.getEvaluationDate(), pojo.getResult());
        List<Analysis> previousAnalysisList = new ArrayList<>();
        for (Analysis analysis : previousAnalysisList) {
            analysis.setLatest(false);
            logger.info("Setting previous analysis with id {} as NOT THE LATEST", analysis.getId());
            analysisJpaRepository.save(analysis);
        }
        pojo.setLatest(true);
        analysisJpaRepository.save(pojo);
    }

    @Override
    public List<Analysis> findAllByProject(Project project) {
        return analysisJpaRepository.findAllByProject(project);
    }

    @Override
    public void remove(String id) {
        Analysis analysis = analysisJpaRepository.getOne(id);
        logger.info("Removed analysis with id: {}", analysis.getId());
        analysisJpaRepository.delete(analysis);
    }

    @Override
    public List<Analysis> findAll() {
        return analysisJpaRepository.findAll();
    }
}
