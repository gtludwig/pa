package ie.gtludwig.pa.service.impl;

import ie.gtludwig.pa.dao.ProjectJpaRepository;
import ie.gtludwig.pa.model.*;
import ie.gtludwig.pa.service.AxisService;
import ie.gtludwig.pa.service.ProjectService;
import ie.gtludwig.pa.service.UserService;
import ie.gtludwig.pa.validation.EmailExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service(value = "projectService")
public class ProjectServiceImpl implements ProjectService {

    private static Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);

    @Autowired
    private ProjectJpaRepository projectJpaRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AxisService axisService;

    @Override
    public List<Project> findAllByCreator(User creator) {
        return projectJpaRepository.findAllByCreator(creator);
    }

//    @Override
//    public User findUserById(String id) {
//        return userService.findById(id);
//    }
//
//    @Override
//    public User findUserByUsername(String username) {
//        return userService.findByUsername(username);
//    }

    @Override
    public User findUserByEmail(String email) {
        return userService.findByEmail(email);
    }

    @Override
    public User findUserById(String userId) {
        return userService.findById(userId);
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

        projectJpaRepository.save(pojo);
    }

    @Override
    public void updateProject(String projectId, String sponsorId, LocalDateTime evaluationStart, LocalDateTime evaluationEnd, String name, String description, int ideal, ProjectState projectState, Set<Axis> axisSet) {
        Project project = findById(projectId);

//        if (axisSet == null) {
//            project.setAxisSet(axisService.createAxisSetForProject(project));
//        }

        project.setSponsor(userService.findById(sponsorId));
        project.setEvaluationStart(evaluationStart);
        project.setEvaluationEnd(evaluationEnd);
        project.setName(name);
        project.setDescription(description);
        project.setIdeal(ideal);
        project.setState(projectState);
        project.setAxisSet(axisSet);

        saveAndFlush(project);
    }

    protected Project saveAndFlush(Project project) {
        boolean isNew = project.getId() == null;

        try {
            project = projectJpaRepository.saveAndFlush(project);

            if (isNew) {
                logger.info("Project with name <<{}>> successfully created.", project.getName());
            } else {
                logger.info("Project with name <<{}>> successfully updated.", project.getName());
            }
        } catch (Exception e) {
            if (isNew) {
                logger.error("Unable to create project with name {}..", project.getName());
            } else {
                logger.info("Unable to update project with name {}.", project.getName());
            }
        }

        return projectJpaRepository.saveAndFlush(project);
    }

//    @Override
//    public Axis findDefaultGuidelineAxis() {
//        return axisService.findDefaultGuidelineAxis();
//    }

    @Override
    public List<Axis> findAllAxis(boolean guideline) {
        return null;
    }

    @Override
    public Project findByName(String name) {
        return projectJpaRepository.findByName(name);
    }

    @Override
    public void createProject(String creatorId, String sponsorId, LocalDateTime creationLocalDateTime, LocalDateTime evaluationStart, LocalDateTime evaluationEnd, String name, String description) {

        Project project = new Project();
        project.setCreator(userService.findById(creatorId));

        project.setSponsor(userService.findById(sponsorId));
        project.setCreationDate(creationLocalDateTime);
        project.setEvaluationStart(evaluationStart);
        project.setEvaluationEnd(evaluationEnd);
        project.setName(name);
        project.setDescription(description);
        project.setCounter(0);
        project.setIdeal(1);
        project.setState(ProjectState.DRAFT);

        project = saveAndFlush(project);

        updateProjectAxis(project);
    }

    protected void updateProjectAxis(Project project) {
        Axis guideline = axisService.createGuidelineAxisForProject(project);
        Set<Axis> axisSet = axisService.createAxisSetForProject(project);

        project.setGuidelineAxis(guideline);
        project.setAxisSet(axisSet);
        save(project);
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

    @Override
    public Project findByGuidelineAxisEqualsOrAxisSet(Axis axis) {
        Project p1 = projectJpaRepository.findByGuidelineAxisEquals(axis);
        Project p2 = projectJpaRepository.findByAxisSetContains(axis);
        return p1 != null ? p1 : p2;
    }

    @Override
    public List<User> findAllSpecialistUsers() {
        return userService.findAllByUserProfileType(UserProfileType.SPECIALIST);
    }

    @Override
    public List<User> findAllSpecialistUsersByProjectId(String projectId) {
        return findById(projectId).getSpecialists().stream().collect(Collectors.toList());
    }

    @Override
    public void inviteSpecialistToProject(String email, String projectId) {
        try {

            userService.createUser(email, "abc1234", "Invited", "Specialist", userService.getInvitedSpecialistUserProfileSet());
        } catch (EmailExistsException e) {
            logger.error(e.getLocalizedMessage());
        }
        User specialist = userService.findByEmail(email);
        final String token = UUID.randomUUID().toString();
        userService.createVerificationTokenForUser(specialist, token);

        Project project = findById(projectId);

        project.getSpecialists().add(specialist);

        save(project);
    }

    @Override
    public void updateProjectSpecialists(Project project) {
        Set<User> persistedSpecialists = findById(project.getId()).getSpecialists();

        if (!persistedSpecialists.equals(project.getSpecialists())) {
            save(project);
        }
    }

    @Override
    public void removeProjectSpecialist(String projectId, String specialistId) {
        Project project = findById(projectId);
        User specialist = userService.findById(specialistId);

        if (project.getSpecialists().contains(specialist)) {
            project.getSpecialists().remove(specialist);
            save(project);
        }

    }
}