package ie.gtludwig.pa.controller;

import ie.gtludwig.pa.controller.dto.RulePojo;
import ie.gtludwig.pa.model.Axis;
import ie.gtludwig.pa.model.Rule;
import ie.gtludwig.pa.service.RuleService;
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
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/project/axis/rule")
@SessionAttributes("pojo")
public class RuleController {

    private static Logger logger = LoggerFactory.getLogger(RuleController.class);

    @Autowired
    private RuleService ruleService;

    @Autowired
    private ApplicationContext context;

    private String entityType = "rule";

    private String lastAction;

    private String buildLastAction(String message, Object[] params) {
        return context.getMessage(message, params, Locale.US);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public void listRules(ModelMap modelMap, @RequestParam(value = "axisId") String axisId, RedirectAttributes redirectAttributes) {
        String projectId = ruleService.findProjectByAxisId(axisId) == null ? "" : ruleService.findProjectByAxisId(axisId).getId();
        Axis axis = ruleService.findAxisByAxisId(axisId);

        modelMap.addAttribute("axisId", axis.getId());
        modelMap.addAttribute("projectId", projectId);
        modelMap.addAttribute("pojoList", populatePojoList(ruleService.findAllFromAxisByAxisId(axisId)));
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public void createRule(ModelMap modelMap, @RequestParam(value = "axisId") String axisId, RedirectAttributes redirectAttributes) {
        RulePojo pojo = new RulePojo();
        Axis axis = ruleService.findAxisByAxisId(axisId);
        pojo.setAxisOrGuideline(axis);

        String projectId = ruleService.findProjectByAxisId(axisId).getId();

        modelMap.addAttribute("axisId", axisId);
        modelMap.addAttribute("projectId", projectId);

        modelMap.addAttribute("pojo", pojo);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createRule(ModelMap modelMap, @Valid @ModelAttribute(value = "pojo") RulePojo pojo, BindingResult bindingResult, Errors errors, final RedirectAttributes redirectAttributes) {
        if(errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                logger.error(errors.getObjectName());
            }
            modelMap.addAttribute("pojo", pojo);
            return "/create?axisId=" + pojo.getAxisOrGuideline().getId();
        }

        lastAction = buildLastAction("createFail", new Object[] {entityType, errors.getAllErrors().toString()});
        try {
            ruleService.createRule(pojo.getAxisOrGuideline().getId(), pojo.getDescription());
            lastAction = buildLastAction("createSuccess", new Object[] {entityType, pojo.getDescription()});
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            logger.error(e.toString());
        }
        logger.info(lastAction);
        redirectAttributes.addFlashAttribute("lastAction", lastAction);
        return "redirect:list?axisId=" + pojo.getAxisOrGuideline().getId();
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public void editRule(ModelMap modelMap, @RequestParam(value = "ruleId") String ruleId) {
        RulePojo pojo = populatePojoFromEntity(ruleService.findById(ruleId));
        modelMap.addAttribute("axisId", pojo.getAxisOrGuideline().getId());
        modelMap.addAttribute("pojo", pojo);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String editRule(ModelMap modelMap, @Valid @ModelAttribute(value = "pojo") RulePojo pojo, BindingResult bindingResult, Errors errors, final RedirectAttributes redirectAttributes) {
        if(errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                logger.error(error.getObjectName());
            }

            System.out.println(pojo.getId());

            modelMap.addAttribute("pojo", pojo);
            return "project/axis/rule/edit?ruleId=" + pojo.getId();
        }

        lastAction = buildLastAction("editFail", new Object[] {entityType, errors.getAllErrors().toString()});
        try {
            ruleService.updateRule(pojo.getId(), pojo.getDescription());
            lastAction = buildLastAction("editSuccess", new Object[]{entityType, pojo.getDescription()});
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            logger.error(e.toString());
        }
        logger.info(lastAction);
        redirectAttributes.addFlashAttribute("lastAction", lastAction);
        return "redirect:list?axisId=" + pojo.getAxisOrGuideline().getId();
    }

    @SuppressWarnings("Duplicates")
    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    public String remove(ModelMap modelMap, @RequestParam(value = "ruleId") String ruleId, final RedirectAttributes redirectAttributes) {
        RulePojo pojo = populatePojoFromEntity(ruleService.findById(ruleId));

        lastAction = buildLastAction("removeFail", new Object[] {entityType, pojo.getDescription()});
        try {
            ruleService.remove(ruleId);
            lastAction = buildLastAction("removeSuccess", new Object[] {pojo.getDescription()});
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        logger.info(lastAction);
        redirectAttributes.addFlashAttribute("lastAction", lastAction);

        return "redirect:list?axisId" + pojo.getAxisOrGuideline().getId();
    }

    private List<RulePojo> populatePojoList(List<Rule> ruleList) {
        List<RulePojo> pojoList = new ArrayList<>();
        for (Rule rule : ruleList) {
            pojoList.add(populatePojoFromEntity(rule));
        }
        return pojoList.stream().sorted(Comparator.comparingInt(RulePojo::getOrdering)).collect(Collectors.toList());
    }

    private RulePojo populatePojoFromEntity(Rule rule) {
        RulePojo pojo = new RulePojo();

        pojo.setId(rule.getId());
        pojo.setOrdering(rule.getOrdering());
        pojo.setDescription(rule.getDescription());
        pojo.setAxisOrGuideline(ruleService.findAxisFromRule(rule));

        if (pojo.getAxisOrGuideline().isApplicationDefault()) {
            pojo.setProject(ruleService.findProjectByAxisId(pojo.getAxisOrGuideline().getId()));
        } else {
            pojo.setProject(null);
        }

        return pojo;
    }
}
