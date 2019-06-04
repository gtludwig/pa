package ie.gtludwig.pa.registration.listener;

import ie.gtludwig.pa.model.User;
import ie.gtludwig.pa.registration.OnRegistrationCompleteEvent;
import ie.gtludwig.pa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private Environment environment;

    // API


    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent onRegistrationCompleteEvent) {
        this.confirmRegistration(onRegistrationCompleteEvent);
    }

    private void confirmRegistration(final OnRegistrationCompleteEvent onRegistrationCompleteEvent) {
        final User user = onRegistrationCompleteEvent.getUser();
        final String token = UUID.randomUUID().toString();
        userService.createVerificationTokenForUser(user, token);

        final SimpleMailMessage email = constructEmailMessage(onRegistrationCompleteEvent, user, token);
        javaMailSender.send(email);
    }

    private final SimpleMailMessage constructEmailMessage(final OnRegistrationCompleteEvent onRegistrationCompleteEvent, final User user, final String token) {
        final String recipientAddress = user.getEmail();
        final String subject = "Registration confirmation";
        final String confirmationUrl = onRegistrationCompleteEvent.getAppUrl() + "/admin/user/registration/confirm?token=" + token;
        final String message = messageSource.getMessage("mail.registrationConfirmation", null, onRegistrationCompleteEvent.getLocale());
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + " \r\n" + confirmationUrl);
        email.setFrom(environment.getProperty("support.email"));
        return email;
    }
}
