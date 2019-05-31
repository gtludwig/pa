package ie.gtludwig.pa.service;

import ie.gtludwig.pa.model.Axis;
import ie.gtludwig.pa.model.Project;
import ie.gtludwig.pa.model.ProjectState;
import ie.gtludwig.pa.model.User;
import ie.gtludwig.pa.service.generic.CrudService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface ProjectService extends CrudService<Project> {

    Project findByName(String name);

    void createProject(String creatorId, String sponsorId, LocalDateTime creationLocalDateTime, LocalDateTime evaluationStart, LocalDateTime evaluationEnd, String name, String description);

    void updateProject(String projectId, String sponsorId, LocalDateTime evaluationStart, LocalDateTime evaluationEnd, String name, String description, int ideal, ProjectState projectState, Set<Axis> axisSet);

//    List<Guideline> findAllGuidelines();
    List<Axis> findAllAxis(boolean guideline);

    List<Project> findAllByCreator(User creator);

    User findUserByEmail(String email);

    User findUserById(String userId);

    List<User> findAllSponsorUsers();

    List<User> findAllSpecialistUsers();

    List<User> findAllSpecialistUsersByProjectId(String projectId);




    Project findByGuidelineAxisEqualsOrAxisSet(Axis axis);

    void inviteSpecialistToProject(String email, String projectId);

    void updateProjectSpecialists(Project project);

    void removeProjectSpecialist(String projectId, String specialistId);

}
