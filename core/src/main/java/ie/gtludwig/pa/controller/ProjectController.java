package ie.gtludwig.pa.controller;

import ie.gtludwig.pa.controller.dto.ProjectPojo;
import ie.gtludwig.pa.model.Project;
import ie.gtludwig.pa.model.User;
import ie.gtludwig.pa.registration.OnInvitationCompleteEvent;
import ie.gtludwig.pa.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping(value = "/project")
@SessionAttributes("pojo")
public class ProjectController {

    private static Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    private String entityType = "project";

    private String lastAction;

    private String buildLastAction(String message, Object[] params) {
        return context.getMessage(message, params, Locale.US);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public void listProjects(ModelMap modelMap, RedirectAttributes redirectAttributes) {
        modelMap.addAttribute("pojoList", populatePojoList(projectService.findAll()));
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public void createProject(ModelMap modelMap) {
        modelMap.addAttribute("pojo",  populateNewDefaultProject());
        modelMap.addAttribute("sponsors", projectService.findAllSponsorUsers());
        modelMap.addAttribute("guidelines", projectService.findAllAxis(true));
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createProject(ModelMap modelMap, @Valid @ModelAttribute(value = "pojo") ProjectPojo pojo, Errors errors, final RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                logger.error(error.getObjectName());
            }
            Project newProject = populateNewDefaultProject();
            newProject.setCreator(pojo.getCreator());
            newProject.setSponsor(pojo.getSponsor());
            newProject.setEvaluationStart(pojo.getEvaluationStart());
            newProject.setEvaluationEnd(pojo.getEvaluationEnd());
            newProject.setName(pojo.getName());
            newProject.setDescription(pojo.getDescription());

            modelMap.addAttribute("pojo", newProject);
            modelMap.addAttribute("sponsors", projectService.findAllSponsorUsers());
            return "project/create";
        }

        lastAction = buildLastAction("createFail", new Object[] {entityType, errors.getAllErrors().toString()});
        try {

            projectService.createProject(
                    pojo.getCreator().getId(),
                    pojo.getSponsor().getId(),
                    pojo.getCreationDate(),
                    pojo.getEvaluationStart(),
                    pojo.getEvaluationEnd(),
                    pojo.getName(),
                    pojo.getDescription()
            );
            lastAction = buildLastAction("createSuccess", new Object[] {entityType, pojo.getName()});
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            logger.error(e.toString());
        }
        logger.info(lastAction);
        redirectAttributes.addFlashAttribute("lastAction", lastAction);
        return "redirect:list";
    }

    protected Project populateNewDefaultProject() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        LocalDateTime creationLocalDateTime = LocalDateTime.now();


        Project pojo = new Project();
        pojo.setCreator(projectService.findUserByEmail(currentPrincipalName));
        pojo.setCreationDate(creationLocalDateTime);
        pojo.setEvaluationStart(creationLocalDateTime);
        pojo.setEvaluationEnd(creationLocalDateTime.plus(10, ChronoUnit.DAYS));

        return pojo;

    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public void editProject(ModelMap modelMap, @RequestParam(value = "id") String id) {
        modelMap.addAttribute("pojo", projectService.findById(id));
        modelMap.addAttribute("sponsors", projectService.findAllSponsorUsers());
        modelMap.addAttribute("guidelines", projectService.findAllAxis(true));
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String editProject(ModelMap modelMap, @Valid @ModelAttribute(value = "pojo") ProjectPojo pojo, BindingResult bindingResult, Errors errors, final RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                logger.error(error.getObjectName());
            }
            modelMap.addAttribute("pojo", pojo);
            return "project/edit?id=" + pojo.getId();
        }

        lastAction = buildLastAction("editFail", new Object[]{entityType, pojo.getName()});
        try {

            projectService.updateProject(
                    pojo.getId(),
                    pojo.getSponsor().getId(),
                    pojo.getEvaluationStart(),
                    pojo.getEvaluationEnd(),
                    pojo.getName(),
                    pojo.getDescription(),
                    pojo.getIdeal(),
                    pojo.getState(),
                    pojo.getAxisSet()
            );
            lastAction = buildLastAction("editSuccess", new Object[]{entityType, pojo.getName()});
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        logger.info(lastAction);
        redirectAttributes.addFlashAttribute("lastAction", lastAction);
        return "redirect:list";
    }

    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    public String remove(ModelMap modelMap, @RequestParam(value = "id") String id, final RedirectAttributes redirectAttributes) {
        Project project = projectService.findById(id);

        lastAction = buildLastAction("removeFail", new Object[] {entityType, project.getName()});
        try {
            projectService.remove(id);
            lastAction = buildLastAction("removeSuccess", new Object[] {project.getName()});
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        logger.info(lastAction);
        redirectAttributes.addFlashAttribute("lastAction", lastAction);
        return "redirect:list";
    }

    @RequestMapping(value = "/specialists", method = RequestMethod.GET)
    public void listSpecialists(ModelMap modelMap, @RequestParam(value = "id") String id) {
        ProjectPojo pojo = populatePojoFromEntity(projectService.findById(id));

        modelMap.addAttribute("projectId", id);
        modelMap.addAttribute("pojo", pojo);
        modelMap.addAttribute("pojoList", projectService.findAllSpecialistUsersByProjectId(id));
        modelMap.addAttribute("allSpecialists", projectService.findAllSpecialistUsers());
    }

    @SuppressWarnings("Duplicates")
    @RequestMapping(value = "/addSpecialists", method = RequestMethod.POST)
    public String addSpecialist(ModelMap modelMap,
                                @Valid @ModelAttribute(value = "pojo") Project project,
                                Errors errors,
                                final RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                logger.error(error.getObjectName());
            }
            modelMap.addAttribute("pojo", project);
            return "/specialists?id=" + project.getId();
        }
        lastAction = buildLastAction("editAttributeFail", new Object[]{"specialists list", entityType, project.getName()});
        try {
            projectService.updateProjectSpecialists(project);
            lastAction = buildLastAction("editAttributeSuccess", new Object[]{"specialists list", entityType, project.getName()});
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            logger.error(e.toString());
        }
        logger.info(lastAction);
        redirectAttributes.addFlashAttribute("lastAction", lastAction);
        return "redirect:specialists?id=" + project.getId();
    }

    @SuppressWarnings("Duplicates")
    @RequestMapping(value = "/inviteSpecialist", method = RequestMethod.POST)
    public String inviteSpecialist(ModelMap modelMap,
                                   @Valid @ModelAttribute(value = "pojo") ProjectPojo pojo,
                                   Errors errors,
                                   HttpServletRequest request,
                                   final RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                logger.error(error.getObjectName());
            }
            modelMap.addAttribute("pojo", pojo);
            return "/specialists?id=" + pojo.getId();
        }
        lastAction = buildLastAction("editAttributeFail", new Object[]{"invited specialists", entityType, pojo.getName()});
        try {
            projectService.inviteSpecialistToProject(pojo.getInviteeEmail(), pojo.getId());
            User specialist = projectService.findUserByEmail(pojo.getInviteeEmail());
            eventPublisher.publishEvent(new OnInvitationCompleteEvent(specialist, Locale.US, getAppUrl(request)));
            lastAction = buildLastAction("editAttributeSuccess", new Object[]{"specialists list", entityType, pojo.getName()});
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            logger.error(e.toString());
        }
        logger.info(lastAction);
        redirectAttributes.addFlashAttribute("lastAction", lastAction);
        return "redirect:specialists?id=" + pojo.getId();
    }

    @RequestMapping(value = "/specialists/remove", method = RequestMethod.GET)
    public String removeSpecialist(ModelMap modelMap,
                                   @RequestParam(value = "projectId") String projectId,
                                   @RequestParam(value = "specialistId") String specialistId,
                                   final RedirectAttributes redirectAttributes) {
        User specialistUser = projectService.findUserById(specialistId);
        String specialist = "specialist " + specialistUser.getFirstName() + " " + specialistUser.getLastName();

        lastAction = buildLastAction("removeAttributeFail", new Object[]{specialist, entityType, projectService.findById(projectId).getName()});
        try {
            projectService.removeProjectSpecialist(projectId, specialistId);
            lastAction = buildLastAction("removeAttributeSuccess", new Object[]{specialist, entityType, projectService.findById(projectId).getName()});
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        logger.info(lastAction);
        redirectAttributes.addFlashAttribute("lastAction", lastAction);
        return "redirect:/project/specialists?id=" + projectId;
    }

    private List<ProjectPojo> populatePojoList(List<Project> projectList) {
        List<ProjectPojo> pojoList = new ArrayList<>();
        for (Project project : projectList) {
            pojoList.add(populatePojoFromEntity(project));
        }
        return pojoList;
    }

    private ProjectPojo populatePojoFromEntity(Project project) {
        ProjectPojo pojo = new ProjectPojo();

        pojo.setId(project.getId());
        pojo.setCreator(project.getCreator());
        pojo.setSponsor(project.getSponsor());
        pojo.setCreationDate(project.getCreationDate());
        pojo.setEvaluationStart(project.getEvaluationStart());
        pojo.setEvaluationEnd(project.getEvaluationEnd());
        pojo.setName(project.getName());
        pojo.setDescription(project.getDescription());
        pojo.setCounter(project.getCounter());
        pojo.setIdeal(project.getIdeal());
        pojo.setState(project.getState());
        pojo.setGuidelineAxis(project.getGuidelineAxis());
        pojo.setAxisSet(project.getAxisSet());
        pojo.setSpecialists(project.getSpecialists());

        return pojo;
    }

    // ============== NON-API ============

    private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
