package ie.gtludwig.pa.controller;

import ie.gtludwig.pa.controller.dto.AxisPojo;
import ie.gtludwig.pa.model.Axis;
import ie.gtludwig.pa.service.AxisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping(value = "/project/axis")
@SessionAttributes("pojo")
public class AxisController {

    private static Logger logger = LoggerFactory.getLogger(AxisService.class);

    @Autowired
    private AxisService axisService;

    @Autowired
    private ApplicationContext context;

    private String entityType = "axis";

    private String lastAction;

    private String buildLastAction(String message, Object[] params) {
        return context.getMessage(message, params, Locale.US);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public void listAxis(ModelMap modelMap, @RequestParam(value = "projectId", required = false) String projectId, RedirectAttributes redirectAttributes) {
        if (projectId != null) {
            modelMap.addAttribute("projectId", projectId);
            modelMap.addAttribute("project", axisService.findProjectByProjectId(projectId));
            modelMap.addAttribute("pojoList", populatePojoList(axisService.findAllByProject(projectId)));
        } else {
            modelMap.addAttribute("pojoList", populatePojoList(axisService.findAllDefaultAxis()));
        }
    }

//    @RequestMapping(value = "/list/addRule/{axisId}", method = RequestMethod.GET)
//    public String addRuleToAxis(ModelMap modelMap, @PathVariable(value = "axisId") String axisId, RedirectAttributes redirectAttributes) {
//        axisService.addRuleToAxis(axisId);
//        modelMap.addAttribute("pojoList", axisService.findAll());
//        return "redirect:list";
//    }

//    @RequestMapping(value = "/list/removeRule/{axisId}", method = RequestMethod.GET)
//    public String removeRuleFromAxis(ModelMap modelMap, @PathVariable(value = "axisId") String axisId, RedirectAttributes redirectAttributes) {
//        axisService.removeRuleFromAxis(axisId);
//        modelMap.addAttribute("pojoList", axisService.findAll());
//        return "/project/axis/list";
//    }



    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public void createAxis(ModelMap modelMap, @RequestParam(value = "projectId", required = false) String projectId, RedirectAttributes redirectAttributes) {
        AxisPojo pojo = new AxisPojo();
        pojo.setApplicationDefault(true);
        if (projectId != null) {
            modelMap.addAttribute("projectId", projectId);
            pojo.setProject(axisService.findProjectByProjectId(projectId));
            pojo.setApplicationDefault(false);
        }
        modelMap.addAttribute("pojo", pojo);
    }

    @SuppressWarnings("Duplicates")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createAxis(ModelMap modelMap, @Valid @ModelAttribute(value = "pojo") AxisPojo pojo, BindingResult bindingResult, Errors errors, final RedirectAttributes redirectAttributes) {
        String projectId = pojo.getProject() == null ? null : pojo.getProject().getId();
        if(errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                logger.error(error.getObjectName());
            }
            if (projectId != null) {
                modelMap.addAttribute("projectId", pojo.getProject().getId());
                pojo.setProject(axisService.findProjectByProjectId(projectId));
            }
            modelMap.addAttribute("pojo", pojo);
            return "/create?projectId=" + projectId;
        }

        lastAction = buildLastAction("createFail", new Object[] {entityType, errors.getAllErrors().toString()});
        try {
            axisService.createAxis(projectId, pojo.getDescription(), pojo.isApplicationDefault(), pojo.isGuideline(), pojo.getNumberOfRules());

            lastAction = buildLastAction("createSuccess", new Object[] {entityType, pojo.getDescription()});
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            logger.error(e.toString());
        }
        logger.info(lastAction);
        redirectAttributes.addFlashAttribute("lastAction", lastAction);

        return projectId != null ? "redirect:list?projectId=" + projectId : "redirect:list";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public void editAxis(ModelMap modelMap, @RequestParam(value = "axisId") String axisId) {
        modelMap.addAttribute("pojo", populatePojoFromEntity(axisService.findById(axisId)));
    }

    @SuppressWarnings("Duplicates")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String editAxis(ModelMap modelMap, @Valid @ModelAttribute(value = "pojo") AxisPojo pojo, BindingResult bindingResult, Errors errors, final RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                logger.error(error.getObjectName());
            }
            if (pojo.getProject().getId() != null) {
                modelMap.addAttribute("projectId", pojo.getProject().getId());
                pojo.setProject(axisService.findProjectByProjectId(pojo.getProject().getId()));
            }
            modelMap.addAttribute("pojo", pojo);
            return "project/axis/edit?id=" + pojo.getId();
        }

        lastAction = buildLastAction("editFail", new Object[] {entityType, pojo.getDescription()});
        try {
            if (pojo.getProject() == null) {
                axisService.updateAxis(pojo.getId(), pojo.getDescription(), pojo.isApplicationDefault(), pojo.isGuideline(), pojo.getNumberOfRules());
//                axisService.updateAxis(pojo.getId(), pojo.getProject().getId(), pojo.getDescription(), pojo.isApplicationDefault(), pojo.isGuideline(), pojo.getNumberOfRules());
            } else {
                axisService.updateAxis(pojo.getId(), pojo.getProject().getId(), pojo.getDescription(), pojo.isApplicationDefault(), pojo.isGuideline(), pojo.getNumberOfRules());
//                axisService.updateAxis(pojo.getId(), pojo.getProject().getId(), pojo.getDescription(), pojo.isApplicationDefault(), pojo.isGuideline(), pojo.getNumberOfRules());
            }
            lastAction = buildLastAction("editSuccess", new Object[]{entityType, pojo.getDescription()});
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            logger.error(e.toString());
        }
        logger.info(lastAction);
        redirectAttributes.addFlashAttribute("lastAction", lastAction);

        if (pojo.getProject() == null) {
            return "redirect:list";
        } else {
            return "redirect:list?projectId" + pojo.getProject().getId();
        }
    }

    @SuppressWarnings("Duplicates")
    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    public String remove(ModelMap modelMap, @RequestParam(value = "id") String id, final RedirectAttributes redirectAttributes) {
        AxisPojo pojo = populatePojoFromEntity(axisService.findById(id));
        String projectId = pojo.getProject().getId();

        lastAction = buildLastAction("removeFail", new Object[] {entityType, pojo.getDescription()});
        try {
            axisService.remove(id);
            lastAction = buildLastAction("removeSuccess", new Object[] {pojo.getDescription()});
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        logger.info(lastAction);
        redirectAttributes.addFlashAttribute("lastAction", lastAction);


        return projectId != null ? "redirect:list?projectId=" + projectId : "redirect:list";
    }


    private List<AxisPojo> populatePojoList(List<Axis> axisList) {
        List<AxisPojo> pojoList= new ArrayList<>();
        for (Axis axis : axisList) {
            pojoList.add(populatePojoFromEntity(axis));
        }
        return pojoList;
    }

    private AxisPojo populatePojoFromEntity(Axis axis) {
        AxisPojo pojo = new AxisPojo();

        pojo.setId(axis.getId());
        pojo.setOrdering(axis.getOrdering());
        pojo.setDescription(axis.getDescription());
        pojo.setApplicationDefault(axis.isApplicationDefault());
        pojo.setGuideline(axis.isGuideline());
        pojo.setNumberOfRules(axis.getNumberOfRules());
        pojo.setRuleSet(axis.getRuleSet());
        pojo.setProject(axisService.findProjectFromAxis(axis));

        return pojo;
    }

}
