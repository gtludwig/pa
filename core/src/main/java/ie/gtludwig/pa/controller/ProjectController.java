package ie.gtludwig.pa.controller;

import ie.gtludwig.pa.model.Project;
import ie.gtludwig.pa.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Locale;

@Controller
@RequestMapping(value = "/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ApplicationContext context;

    private String lastAction;

    private static Logger logger = LoggerFactory.getLogger(ProjectController.class);

    private String buildLastAction(String[] params) {
        return context.getMessage(params[0], new Object[] {params[1]}, Locale.US);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public void listProjects(ModelMap modelMap, RedirectAttributes redirectAttributes) {
        modelMap.addAttribute("pojo", new Project());
        modelMap.addAttribute("pojoList", projectService.findAll());
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public void createProject(ModelMap modelMap) {
        modelMap.addAttribute("pojo", new Project());
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createProject(ModelMap modelMap, @Valid @ModelAttribute(value = "pojo") Project project, BindingResult bindingResult, Errors errors, final RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                logger.error(error.getObjectName());
            }
            modelMap.addAttribute("pojo", project);
            return "project/create";
        }

        lastAction = buildLastAction(new String[] {"project.createFail", ""});
        try {
            projectService.save(project);
            lastAction = buildLastAction(new String[] {"project.createSuccess", project.getDescription()});
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        logger.info(lastAction);
        redirectAttributes.addFlashAttribute("lastAction", lastAction);
        return "redirect:list";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public void editProject(ModelMap modelMap, @RequestParam(value = "id") String id) {
        modelMap.addAttribute("pojo", projectService.findById(id));
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String editProject(ModelMap modelMap, @Valid @ModelAttribute(value = "pojo") Project project, BindingResult bindingResult, Errors errors, final RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                logger.error(error.getObjectName());
            }
            modelMap.addAttribute("pojo", project);
            return "/project/edit?id=" + project.getId();
        }

        lastAction = buildLastAction(new String[] {"project.editFail", project.getName()});
        try {
            projectService.updateProject(project);
            lastAction = buildLastAction(new String[] {"project.editSuccess", project.getName()});
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        logger.info(lastAction);
        redirectAttributes.addFlashAttribute("lastAction", lastAction);
        return "redirect:list";
    }

    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    public String remove(ModelMap modelMap, @RequestParam(value = "id") String id, final RedirectAttributes redirectAttributes) {
        String project = projectService.findById(id).toString();
        lastAction = buildLastAction(new String[] {"project.removeFail", ""});
        try {
            projectService.remove(id);
            lastAction = buildLastAction(new String[] {"project.removeSucess", project});
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        logger.info(lastAction);
        redirectAttributes.addFlashAttribute("lastAction", lastAction);
        return "redirect:list";
    }

}
