package ie.gtludwig.pa.service.impl;

import ie.gtludwig.pa.dao.ProjectJpaRepository;
import ie.gtludwig.pa.model.*;
import ie.gtludwig.pa.service.AxisService;
import ie.gtludwig.pa.service.GuidelineService;
import ie.gtludwig.pa.service.ProjectService;
import ie.gtludwig.pa.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service(value = "projectService")
public class ProjectServiceImpl implements ProjectService {

    private static Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);

    @Autowired
    private ProjectJpaRepository projectJpaRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private GuidelineService guidelineService;

    @Autowired
    private AxisService axisService;

    @Override
    public List<Project> findAllByCreator(User creator) {
        return projectJpaRepository.findAllByCreator(creator);
    }

    @Override
    public User findUserById(String id) {
        return userService.findById(id);
    }

    @Override
    public User findUserByUsername(String username) {
        return userService.findByUsername(username);
    }

    @Override
    public User findUserByEmail(String email) {
        return userService.findByEmail(email);
    }

    @Override
    public List<User> findAllSponsorUsers() {
        return userService.findAllByUserProfileType(UserProfileType.SPONSOR);
    }

    @Override
    public Project findById(String id) {
        return projectJpaRepository.getOne(id);
    }

    @Override
    public void save(Project pojo) {
        logger.info("Saved project with name: " + pojo.getName());
        pojo.setGuideline(guidelineService.findByDescription("Default Guideline Description"));

        projectJpaRepository.save(pojo);
    }

    @Override
    public void updateProject(String projectId, String sponsorId, LocalDateTime evaluationStart, LocalDateTime evaluationEnd, String name, String description, int ideal, ProjectState projectState, String guidelineId, Set<Axis> axisSet) {
        Project project = findById(projectId);

        if (axisSet == null) {
            project.setAxisSet(axisService.createDefaultAxisSetFromProject(project));
        }

        try {
            project.setSponsor(userService.findById(sponsorId));
            project.setEvaluationStart(evaluationStart);
            project.setEvaluationEnd(evaluationEnd);
            project.setName(name);
            project.setDescription(description);
            project.setIdeal(ideal);
            project.setState(projectState);
            project.setGuideline(guidelineService.findById(guidelineId));
            project.setAxisSet(axisSet);

            save(project);
            logger.info("Project with name <<{}>> successfully updated.", name.toUpperCase());

        } catch (Exception e) {
            logger.error("Unable to update project with name {}.", name);
        }
    }

    @Override
    public List<Guideline> findAllGuidelines() {
        return guidelineService.findAll();
    }

    @Override
    public Project findByName(String name) {
        return projectJpaRepository.findByName(name);
    }

    @Override
    public void createProject(String creatorId, String sponsorId, LocalDateTime creationLocalDateTime, LocalDateTime evaluationStart, LocalDateTime evaluationEnd, String name, String description) {

        try {
            save(new Project(userService.findById(creatorId), userService.findById(sponsorId), creationLocalDateTime, evaluationStart, evaluationEnd, name, description, 0, 1, ProjectState.DRAFT));
            logger.info("Project with name <<{}>> successfully created.", name.toUpperCase());


        } catch (Exception e) {
            logger.error("Unable to create project with name <<{}>>.", name);
        }
    }

    @Override
    public void remove(String id) {
        Project project = projectJpaRepository.getOne(id);
        logger.info("Removed project with name: " + project.getName());
        projectJpaRepository.delete(project);
    }

    @Override
    public List<Project> findAll() {
        List<Project> projects = projectJpaRepository.findAll();
        return projects;
    }
}