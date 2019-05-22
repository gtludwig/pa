package ie.gtludwig.pa.service;

import ie.gtludwig.pa.model.*;
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

    List<User> findAllSponsorUsers();

}
