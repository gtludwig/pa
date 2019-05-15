package ie.gtludwig.pa.controller;

import ie.gtludwig.pa.config.UserValidator;
import ie.gtludwig.pa.model.User;
import ie.gtludwig.pa.service.SecurityService;
import ie.gtludwig.pa.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Locale;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private ApplicationContext context;

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    private String lastAction;


    @GetMapping(value = "/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new User());
        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult) {
        userValidator.validate(userForm, bindingResult);

        if(bindingResult.hasErrors()) {
            return "registration";
        }

        userService.save(userForm);

        securityService.autologin(userForm.getUsername(), userForm.getPassword());

        return "redirect:/welcome";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            System.out.println(">>>>>>>>>>>>>>>>>>> LOGOUT <<<<<<<<<<<<<<<<<<");
            model.addAttribute("message", "You have been logged out successfully.");

        return "login";
    }

    private String getPrincipal(){
        String userName = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userName = ((UserDetails)principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }

    private String buildLastAction(String[] params) {
        return context.getMessage(params[0], new Object[] {params[1]}, Locale.US);
    }

    @RequestMapping(value = "/welcome", method = RequestMethod.GET)
    public String welcome(Model model) {
        return "welcome";
    }

    @RequestMapping(value = "/user/list", method = RequestMethod.GET)
    public void listUsers(ModelMap model, RedirectAttributes redirectAttributes) {
        model.addAttribute("user", getPrincipal());
        model.addAttribute("userList", userService.findAll());
    }

    @RequestMapping(value = "/user/create", method = RequestMethod.GET)
    public void createUser(ModelMap model) {
        model.addAttribute("pojo", new User());
        model.addAttribute("allProfiles", userService.findAllUserProfiles());

    }

    @RequestMapping(value = "/user/create", method = RequestMethod.POST)
    public String createUser(ModelMap model, @Valid @ModelAttribute(value = "pojo") User user, BindingResult bindingResult, Errors errors, final RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                logger.error(error.getObjectName());
            }
            model.addAttribute("pojo", user);
            return "/user/create";
        }
        lastAction = buildLastAction(new String[] {"user.createFail", ""});

        try {
            userService.save(user);
            lastAction = buildLastAction(new String[] {"user.createSuccess", user.getUsername()});
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        logger.info(lastAction);
        redirectAttributes.addFlashAttribute("lastAction", lastAction);
        return "redirect:list";
    }

    @RequestMapping(value = "user/edit", method = RequestMethod.GET)
    public void editUser(ModelMap model, @RequestParam(value = "id") String id) {
        model.addAttribute("pojo", userService.findById(id));
        model.addAttribute("allProfiles", userService.findAllUserProfiles());
    }

    @RequestMapping(value = "/user/edit", method = RequestMethod.POST)
    public String editUser(ModelMap model, @Valid @ModelAttribute(value = "pojo") User user, BindingResult bindingResult, Errors errors, final RedirectAttributes redirectAttributes) {
                if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                logger.error(error.getObjectName());
            }
            model.addAttribute("pojo", user);
            return "/user/edit?id=" + user.getId();
        }
        lastAction = buildLastAction(new String[] {"user.editFail", ""});
        try {
            userService.save(user);
            lastAction = buildLastAction(new String[] {"user.editSuccess", user.getUsername()});
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        logger.info(lastAction);
        redirectAttributes.addFlashAttribute("lastAction", lastAction);
        return "redirect:list";
    }

    @RequestMapping(value = "/user/remove", method = RequestMethod.GET)
    public String remove(ModelMap model, @RequestParam(value = "id") String id, final RedirectAttributes redirectAttributes) {
        String user = userService.findById(id).toString();
        lastAction = buildLastAction(new String[] {"user.removeFail", ""});
        try {
            userService.remove(id);
            lastAction = buildLastAction(new String[] {"user.removeSuccess", user});
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        logger.info(lastAction);
        redirectAttributes.addFlashAttribute("lastAction", lastAction);
        return "redirect:list";
    }
}
