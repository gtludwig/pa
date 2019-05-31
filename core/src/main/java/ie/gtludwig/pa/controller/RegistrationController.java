package ie.gtludwig.pa.controller;

import ie.gtludwig.pa.config.UserValidator;
import ie.gtludwig.pa.controller.dto.UserPojo;
import ie.gtludwig.pa.error.UserAlreadyExistException;
import ie.gtludwig.pa.model.Privilege;
import ie.gtludwig.pa.model.User;
import ie.gtludwig.pa.model.UserProfile;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
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

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration(Model model) {
        model.addAttribute("pojo", new UserPojo());
        return "registration";
    }

    @RequestMapping(value = "/user/registration", method = RequestMethod.POST)
    @ResponseBody
    public GenericResponse registerUserAccount(
            @Valid UserPojo pojo, HttpServletRequest request, Errors errors) {
        logger.debug("Registering user account with information: {}", pojo);

        lastAction = buildLastAction("createFail", new Object[]{entityType, errors.getAllErrors().toString()});
        try {
            Set<UserProfile> userProfileSet = userService.getSelfRegistrationUserProfileSet();
            userService.createUser(pojo.getUsername(), pojo.getEmail(), pojo.getFirstName(), pojo.getLastName(), pojo.getPassword(), userProfileSet);
            lastAction = buildLastAction("createSuccess", new Object[]{entityType, pojo.getUsername()});
        } catch (EmailExistsException eee) {
            logger.error(eee.getLocalizedMessage());
            logger.error(eee.toString());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            logger.error(e.toString());
        }

        User registered = userService.findByUsername(pojo.getUsername());

        if (registered == null) {
            throw new UserAlreadyExistException();
        }

        String appUrl = "http://" + request.getServerName() + ":" +
                request.getServerPort() + request.getContextPath();

        eventPublisher.publishEvent(
                new OnRegistrationCompleteEvent(registered, request.getLocale(), appUrl));

        return new GenericResponse("success");
    }


    @RequestMapping(value = "/registrationConfirm", method = RequestMethod.GET)
    public String confirmRegistration(WebRequest request, ModelMap modelMap, @RequestParam(value = "token") String token, Locale locale) {

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
        userService.save(user);
        return "redirect:/login";
    }

    @RequestMapping(value = "/user/resendRegistrationToken", method = RequestMethod.GET)
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
        final String url = contextPath + "/user/changePassword?id=" + user.getId() + "&token=" + token;
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

    public void authWithoutPassword(User user) {
        List<Privilege> privileges = user.getRoles().stream().map(role -> role.getPrivileges()).flatMap(list -> list.stream()).distinct().collect(Collectors.toList());
        List<GrantedAuthority> authorities = privileges.stream().map(p -> new SimpleGrantedAuthority(p.getName())).collect(Collectors.toList());

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
