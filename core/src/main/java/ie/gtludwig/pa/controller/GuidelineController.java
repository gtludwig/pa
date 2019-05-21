package ie.gtludwig.pa.controller;

import ie.gtludwig.pa.model.Guideline;
import ie.gtludwig.pa.service.GuidelineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
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
@RequestMapping(value = "/project/guideline")
public class GuidelineController {

    @Autowired
    private GuidelineService guidelineService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ApplicationContext context;

    private String lastAction;

    private static Logger logger = LoggerFactory.getLogger(GuidelineController.class);

    private String buildLastAction(Object[] params) {
//        return context.getMessage((String) params[0], params, Locale.US);
        return messageSource.getMessage((String) params[0], params, Locale.US);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public void listGuidelines(ModelMap modelMap, RedirectAttributes redirectAttributes) {
        modelMap.addAttribute("pojoList", guidelineService.findAll());
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public void createGuideline(ModelMap modelMap) {
        modelMap.addAttribute("pojo",  new Guideline());
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createGuideline(ModelMap modelMap, @Valid @ModelAttribute(value = "pojo") Guideline pojo, Errors errors, final RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                logger.error(error.getObjectName());
            }
            modelMap.addAttribute("pojo",  pojo);
        return "project/guideline/create";
        }

        lastAction = context.getMessage("guideline.createFail", new Object[] { errors.getAllErrors() }, Locale.US);
//        lastAction = buildLastAction(new Object[] {"guideline.createFail", errors.getAllErrors().toString()});
        try {
            guidelineService.createGuideline(pojo.getName(), pojo.getDescription(), pojo.getNumberOfRules());
            lastAction = context.getMessage("guideline.createSuccess", new Object[] { pojo.getName() }, Locale.US);
//            lastAction = buildLastAction(new String[] {"guideline.createSuccess", pojo.getName()});
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            logger.error(e.toString());
        }
//        logger.info(lastAction);
        redirectAttributes.addFlashAttribute("lastAction", lastAction);

        return "redirect:list";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public void editGuideline(ModelMap modelMap, @RequestParam(value = "id") String id) {
        Guideline pojo = guidelineService.findById(id);
        System.out.println(pojo);
        pojo.setNumberOfRules(pojo.getRulesSet().size());
        modelMap.addAttribute("pojo", pojo);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String editGuideline(ModelMap modelMap, @Valid @ModelAttribute(value = "pojo") Guideline pojo, BindingResult bindingResult, Errors errors, final RedirectAttributes redirectAttributes) {
        if(errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                logger.error(error.getObjectName());
            }
            modelMap.addAttribute("pojo", pojo);
            return "project/guideline/edit?id=" + pojo.getId();
        }

        lastAction = buildLastAction(new String[] {"guideline.editFail", pojo.getName()});

        try {
            guidelineService.updateGuideline(pojo.getId(), pojo.getName(), pojo.getDescription(), pojo.getRulesSet().size());
            lastAction = buildLastAction(new String[] {"guideline.editSuccess", pojo.getName()});
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            logger.error(e.toString());
        }

        logger.info(lastAction);
        redirectAttributes.addFlashAttribute("lastAction", lastAction);
        return "redirect:list";
    }

    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    public String remove(ModelMap modelMap, @RequestParam(value = "id") String id, final RedirectAttributes redirectAttributes) {
        String guideline = guidelineService.findById(id).toString();
        lastAction = buildLastAction(new String[] {"guideline.removeFail", ""});
        try {
            guidelineService.remove(id);
            lastAction = buildLastAction(new String[] {"guideline.removeSuccess", guideline});
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        logger.info(lastAction);
        redirectAttributes.addFlashAttribute("lastAction", lastAction);
        return "redirect:list";
    }
}
