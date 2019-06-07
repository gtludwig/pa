package ie.gtludwig.pa.registration.listener;

import ie.gtludwig.pa.model.User;
import ie.gtludwig.pa.registration.OnInvitationCompleteEvent;
import ie.gtludwig.pa.service.MailService;
import ie.gtludwig.pa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class InvitationListener implements ApplicationListener<OnInvitationCompleteEvent> {

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    // API

    @Override
    public void onApplicationEvent(OnInvitationCompleteEvent onInvitationCompleteEvent) {
        this.confirmRegistration(onInvitationCompleteEvent);
    }

    private void confirmRegistration(final OnInvitationCompleteEvent onInvitationCompleteEvent) {
        final User user = onInvitationCompleteEvent.getUser();
        final String token = UUID.randomUUID().toString();
        userService.createVerificationTokenForUser(user, token);


        mailService.sendInvitedUserRegistrationToken(onInvitationCompleteEvent, user, token);

        //TODO fix this!
//        mailService.sendInvitedUserRegistrationToken(onInvitationCompleteEvent, user, token);
    }
}
