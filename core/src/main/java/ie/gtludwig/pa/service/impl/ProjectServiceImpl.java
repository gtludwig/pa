package ie.gtludwig.pa.service.impl;

import ie.gtludwig.pa.dao.ProjectJpaRepository;
import ie.gtludwig.pa.model.Axis;
import ie.gtludwig.pa.model.Guideline;
import ie.gtludwig.pa.model.Project;
import ie.gtludwig.pa.model.User;
import ie.gtludwig.pa.service.AxisService;
import ie.gtludwig.pa.service.GuidelineService;
import ie.gtludwig.pa.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "projectService")
public class ProjectServiceImpl implements ProjectService {

    private static Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);

    @Autowired
    private ProjectJpaRepository projectJpaRepository;

    @Autowired
    private GuidelineService guidelineService;

    @Autowired
    private AxisService axisService;

    @Override
    public List<Project> findAllByCreator(User creator) {
        return projectJpaRepository.findAllByCreator(creator);
    }

    @Override
    public Project findById(String id) {
        return projectJpaRepository.getOne(id);
    }

    @Override
    public void save(Project pojo) {
        logger.info("Saved project with name: " + pojo.getName());
        projectJpaRepository.save(pojo);

        createDefaultGuidelineFromProject(pojo);
        createDefaultAxisFromProject(pojo);
    }

    @Override
    public void updateProject(Project project)  {
        projectJpaRepository.save(project);
    }


    protected void createDefaultGuidelineFromProject(Project project) {
        Guideline guideline = new Guideline("Project " + project.getDescription() + " default guideline", project);
        guidelineService.save(guideline);
    }

    protected void createDefaultAxisFromProject(Project project) {
        axisService.createDefaultAxisSetFromProject(project);
    }

    @Override
    public void remove(String id) {
        Project project = projectJpaRepository.getOne(id);
        logger.info("Removed project with name: " + project.getName());
        projectJpaRepository.delete(project);
    }

    @Override
    public List<Project> findAll() {
        return projectJpaRepository.findAll();
    }
}