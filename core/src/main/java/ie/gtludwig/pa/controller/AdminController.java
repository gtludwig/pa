package ie.gtludwig.pa.controller;

import ie.gtludwig.pa.controller.dto.UserPojo;
import ie.gtludwig.pa.model.User;
import ie.gtludwig.pa.service.UserService;
import ie.gtludwig.pa.validation.EmailExistsException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping(value = "/admin")
@SessionAttributes("pojo")
public class AdminController {

    private static Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationContext context;

    private String entityType;

    private String lastAction;

    private String buildLastAction(String message, Object[] params) {
        return context.getMessage(message, params, Locale.US);
    }

    @RequestMapping(value = "/welcome", method = RequestMethod.GET)
    public String welcome(ModelMap modelMap) {
        return "welcome";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(ModelMap modelMap, String error, String logout) {
        if (error != null)
            modelMap.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            modelMap.addAttribute("message", "You have been logged out successfully.");

        return "login";
    }

    @RequestMapping(value ="/adminHome", method = RequestMethod.GET)
    public void adminHome(ModelMap modelMap) {
        modelMap.addAttribute("pojo", getPrincipal());
    }

    @RequestMapping(value = "/user/list", method = RequestMethod.GET)
    public void listUsers(ModelMap modelMap, RedirectAttributes redirectAttributes) {
        String loggedUser = getPrincipal();
        modelMap.addAttribute("user", loggedUser);
        modelMap.addAttribute("pojoList", populatePojoList(userService.findAll()));
        modelMap.addAttribute("profileList", userService.findAllUserProfiles(loggedUser));
    }

    @RequestMapping(value = "/user/create", method = RequestMethod.GET)
    public void createUser(ModelMap modelMap) {
        modelMap.addAttribute("pojo", new UserPojo());
        modelMap.addAttribute("profileList", userService.findAllUserProfiles(getPrincipal()));
    }

    @SuppressWarnings("Duplicates")
    @RequestMapping(value = "/user/create", method = RequestMethod.POST)
    public String createUser(ModelMap modelMap, @Valid @ModelAttribute(value = "pojo") UserPojo pojo, BindingResult bindingResult, Errors errors, final RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                logger.error(error.getObjectName());
            }
            pojo.setPassword(null);
            pojo.setPasswordConfirm(null);
            modelMap.addAttribute("pojo", pojo);
            return "/admin/user/create";
        }

        entityType = "user";

        lastAction = buildLastAction("createFail", new Object[]{entityType, pojo.getEmail()});
        try {
            userService.createUser(pojo.getEmail(), pojo.getPassword(), pojo.getFirstName(), pojo.getLastName(), pojo.getUserProfileSet());
            lastAction = buildLastAction("createSuccess", new Object[]{entityType, pojo.getEmail()});
        } catch (EmailExistsException eee) {
            logger.error(eee.getLocalizedMessage());
            logger.error(eee.toString());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            logger.error(e.toString());
        }
        logger.info(lastAction);
        redirectAttributes.addFlashAttribute("lastAction", lastAction);
        return "redirect:list";
    }

    @RequestMapping(value = "/user/edit", method = RequestMethod.GET)
    public void editUser(ModelMap modelMap, @RequestParam(value = "id") String id) {
        UserPojo pojo = populatePojoFromEntity(userService.findById(id));
        modelMap.addAttribute("pojo", pojo);
        modelMap.addAttribute("profileList", userService.findAllUserProfiles());
    }

    @SuppressWarnings("Duplicates")
    @RequestMapping(value = "/user/edit", method = RequestMethod.POST)
    public String editUser(ModelMap modelMap, @Valid @ModelAttribute(value = "pojo") UserPojo pojo, BindingResult bindingResult, Errors errors, final RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                logger.error(error.getObjectName());
            }
            modelMap.addAttribute("pojo", pojo);
        }
        entityType = "user";

        lastAction = buildLastAction("editFail", new Object[]{entityType, pojo.getEmail()});
        try {
            if (!pojo.isAccountOwner() || pojo.getPassword().isEmpty()) {
                userService.updateUser(pojo.getId(), pojo.getEmail(), pojo.getFirstName(), pojo.getLastName(), pojo.getUserProfileSet());
            } else {
                userService.updateUser(pojo.getId(), pojo.getEmail(), pojo.getPassword(), pojo.getFirstName(), pojo.getLastName(), pojo.getUserProfileSet());
            }
            lastAction = buildLastAction("editSuccess", new Object[]{entityType, pojo.getEmail()});
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            logger.error(e.toString());
        }
        logger.info(lastAction);
        redirectAttributes.addFlashAttribute("lastAction", lastAction);
        return "redirect:list";
    }

    @RequestMapping(value = "/user/remove", method = RequestMethod.GET)
    public String removeUser(ModelMap modelMap, @RequestParam(value = "id") String id, final RedirectAttributes redirectAttributes) {
        UserPojo pojo = populatePojoFromEntity(userService.findById(id));
        entityType = "user";

        lastAction = buildLastAction("removeFail", new Object[]{entityType, pojo.getEmail()});
        try {
            userService.remove(id);
            lastAction = buildLastAction("removeSuccess", new Object[]{entityType, pojo.getEmail()});
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            logger.error(e.toString());
        }
        logger.info(lastAction);
        redirectAttributes.addFlashAttribute("lastAction", lastAction);
        return "redirect:list";
    }

    private String getPrincipal(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    private List<UserPojo> populatePojoList(List<User> userList) {
        List<UserPojo> pojoList = new ArrayList<>();
        for (User user : userList) {
            pojoList.add(populatePojoFromEntity(user));
        }
        return pojoList;
    }

    private UserPojo populatePojoFromEntity(User user) {
        UserPojo pojo = new UserPojo();

        pojo.setId(user.getId());
        pojo.setEmail(user.getEmail());
        pojo.setFirstName(user.getFirstName());
        pojo.setLastName(user.getLastName());
        pojo.setState(user.getState());
        pojo.setUserProfileSet(user.getUserProfileSet());
        pojo.setAccountOwner(getPrincipal().equalsIgnoreCase(user.getEmail()));


        return pojo;
    }
}
