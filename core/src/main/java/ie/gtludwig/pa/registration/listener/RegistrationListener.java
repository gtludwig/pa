package ie.gtludwig.pa.registration.listener;

import ie.gtludwig.pa.model.User;
import ie.gtludwig.pa.registration.OnRegistrationCompleteEvent;
import ie.gtludwig.pa.service.MailService;
import ie.gtludwig.pa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    // API

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent onRegistrationCompleteEvent) {
        this.confirmRegistration(onRegistrationCompleteEvent);
    }

    private void confirmRegistration(final OnRegistrationCompleteEvent onRegistrationCompleteEvent) {
        final User user = onRegistrationCompleteEvent.getUser();
        final String token = UUID.randomUUID().toString();
        userService.createVerificationTokenForUser(user, token);


//        mailService.sendNewUserRegistrationToken(onRegistrationCompleteEvent, user, token);

        //TODO fix this!
        mailService.sendNewUserRegistrationToken_HTML(onRegistrationCompleteEvent, user, token);
    }
}
