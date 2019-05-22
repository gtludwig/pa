package ie.gtludwig.pa.controller;

import ie.gtludwig.pa.service.AxisService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

@Controller
@RequestMapping(value = "/project/axis")
public class AxisController {

    private static Logger logger = LoggerFactory.getLogger(AxisService.class);

    @Autowired
    private AxisService axisService;

    @Autowired
    private ApplicationContext context;

    private String lastAction;

    private String buildLastAction(String[] params) {
        return context.getMessage(params[0], new Object[] {params[1]}, Locale.US);
    }

    @RequestMapping(value = {"/list","/list/{projectId}"}, method = RequestMethod.GET)
    public void listAxis(ModelMap modelMap, @PathVariable(value = "projectId", required = false) String projectId, RedirectAttributes redirectAttributes) {
        modelMap.addAttribute("pojoList", projectId == null || projectId.isEmpty() ? axisService.findAll() :axisService.findAllByProjectId(projectId));
    }

}
