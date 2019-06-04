package ie.gtludwig.pa.controller;

import ie.gtludwig.pa.config.UserValidator;
import ie.gtludwig.pa.controller.dto.UserPojo;
import ie.gtludwig.pa.model.User;
import ie.gtludwig.pa.model.UserState;
import ie.gtludwig.pa.model.VerificationToken;
import ie.gtludwig.pa.registration.OnRegistrationCompleteEvent;
import ie.gtludwig.pa.service.SecurityService;
import ie.gtludwig.pa.service.UserService;
import ie.gtludwig.pa.util.GenericResponse;
import ie.gtludwig.pa.validation.EmailExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Locale;

@Controller
@RequestMapping(value = "/admin/user/registration")
public class RegistrationController {

    private static Logger logger = LoggerFactory.getLogger(RegistrationController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private UserValidator userValidator;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ApplicationContext context;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private Environment environment;
    @Autowired
    private AuthenticationManager authenticationManager;
    private String entityType = "user";

    private String lastAction;

    private String buildLastAction(String message, Object[] params) {
        return context.getMessage(message, params, Locale.US);
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public void registration(ModelMap modelMap) {
        modelMap.addAttribute("pojo", new UserPojo());
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerUserAccount(@ModelAttribute(value = "pojo") UserPojo pojo,
                                      BindingResult bindingResult,
                                      HttpServletRequest request,
                                      Errors errors,
                                      final RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                logger.error(error.getObjectName());
            }
            return "/admin/user/register/new";
        }

        if (bindingResult.hasErrors()) {
            registration(new ModelMap());
        }

        lastAction = buildLastAction("createFail", new Object[]{entityType, errors.getAllErrors().toString()});
        try {
            String appUrl = getAppUrl(request);
            User user = userService.newUserRegistration(pojo.getEmail(), pojo.getFirstName(), pojo.getLastName(), pojo.getPassword());
            lastAction = buildLastAction("createSuccess", new Object[]{entityType, pojo.getUsername()});
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, Locale.US, appUrl));

        } catch (EmailExistsException eee) {
            logger.error(eee.getLocalizedMessage());
            logger.error(eee.toString());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            logger.error(e.toString());
        }
        logger.info(lastAction);
        redirectAttributes.addFlashAttribute("lastAction", lastAction);
        return "login";
    }

    @RequestMapping(value = "/confirm", method = RequestMethod.GET)
    public String confirmRegistration(ModelMap modelMap, @RequestParam(value = "token") String token) {
        Locale locale = Locale.US;


        VerificationToken verificationToken = userService.getVerificationToken(token);
        if (verificationToken == null) {
            String message = messageSource.getMessage("auth.message.invalidToken", null, locale);
            modelMap.addAttribute("message", message);
            modelMap.addAttribute("expired", true);
            modelMap.addAttribute("token", token);
            return "redirect:/badUser.html?lang=" + locale.getLanguage();
        }

        User user = verificationToken.getUser();
        Calendar calendar = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - calendar.getTime().getTime()) <= 0) {
            String message = messageSource.getMessage("auth.message.expired", null, locale);
            modelMap.addAttribute("message", message);
            return "redirect:/badUser";
        }

        user.setEnabled(true);
        user.setState(UserState.ACTIVE.getState());
        userService.save(user);
        return "login";
    }

    @RequestMapping(value = "/resendRegistrationToken", method = RequestMethod.GET)
    @ResponseBody
    public GenericResponse resendRegistrationToken(HttpServletRequest httpServletRequest, @RequestParam(value = "token") String existingToken) {
        VerificationToken newToken = userService.generateNewVerificationToken(existingToken);

        User user = userService.getUser(newToken.getToken());
        String appUrl =
                "http://" + httpServletRequest.getServerName() +
                        ":" + httpServletRequest.getServerPort() +
                        httpServletRequest.getContextPath();
        SimpleMailMessage email =
                constructResendVerificationTokenEmail(appUrl, httpServletRequest.getLocale(), newToken, user);
        javaMailSender.send(email);

        return new GenericResponse(messageSource.getMessage("message.resendToken", null, httpServletRequest.getLocale()));
    }


    // ============== NON-API ============

    private SimpleMailMessage constructResendVerificationTokenEmail(final String contextPath, final Locale locale, final VerificationToken newToken, final User user) {
        final String confirmationUrl = contextPath + "/registrationConfirm.html?token=" + newToken.getToken();
        final String message = messageSource.getMessage("message.resendToken", null, locale);
        return constructEmail("Resend Registration Token", message + " \r\n" + confirmationUrl, user);
    }

    private SimpleMailMessage constructResetTokenEmail(final String contextPath, final Locale locale, final String token, final User user) {
        final String url = contextPath + "/changePassword?id=" + user.getId() + "&token=" + token;
        final String message = messageSource.getMessage("message.resetPassword", null, locale);
        return constructEmail("Reset Password", message + " \r\n" + url, user);
    }

    private SimpleMailMessage constructEmail(String subject, String body, User user) {
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(user.getEmail());
        email.setFrom(environment.getProperty("support.email"));
        return email;
    }

    private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    public void authWithHttpServletRequest(HttpServletRequest request, String username, String password) {
        try {
            request.login(username, password);
        } catch (ServletException e) {
            logger.error("Error while login ", e);
        }
    }

    public void authWithAuthManager(HttpServletRequest request, String username, String password) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
        authToken.setDetails(new WebAuthenticationDetails(request));
        Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
    }

//    public void authWithoutPassword(User user) {
//        List<Privilege> privileges = user.getRoles().stream().map(role -> role.getPrivileges()).flatMap(list -> list.stream()).distinct().collect(Collectors.toList());
//        List<GrantedAuthority> authorities = privileges.stream().map(p -> new SimpleGrantedAuthority(p.getName())).collect(Collectors.toList());
//
//        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//    }
}
