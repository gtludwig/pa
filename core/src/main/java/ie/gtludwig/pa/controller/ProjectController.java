package ie.gtludwig.pa.controller;

import ie.gtludwig.pa.model.Project;
import ie.gtludwig.pa.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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

    private String entityType = "project";

    private String lastAction;

    private String buildLastAction(String message, Object[] params) {
        return context.getMessage(message, params, Locale.US);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public void listProjects(ModelMap modelMap, RedirectAttributes redirectAttributes) {
        modelMap.addAttribute("pojoList", projectService.findAll());
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public void createProject(ModelMap modelMap) {
        modelMap.addAttribute("pojo",  populateNewDefaultProject());
        modelMap.addAttribute("sponsors", projectService.findAllSponsorUsers());
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createProject(ModelMap modelMap, @Valid @ModelAttribute(value = "pojo") Project pojo, Errors errors, final RedirectAttributes redirectAttributes) {
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
    public String editProject(ModelMap modelMap, @Valid @ModelAttribute(value = "pojo") Project project, BindingResult bindingResult, Errors errors, final RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                logger.error(error.getObjectName());
            }
            modelMap.addAttribute("pojo", project);
            return "project/edit?id=" + project.getId();
        }

        lastAction = buildLastAction("editFail", new Object[] {entityType, project.getName()});
        try {

            projectService.updateProject(
                    project.getId(),
                    project.getSponsor().getId(),
                    project.getEvaluationStart(),
                    project.getEvaluationEnd(),
                    project.getName(),
                    project.getDescription(),
                    project.getIdeal(),
                    project.getState(),
                    project.getAxisSet()
            );
            lastAction = buildLastAction("editSuccess", new Object[] {entityType, project.getName()});
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
}
