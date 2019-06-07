package ie.gtludwig.pa.registration.listener;

import ie.gtludwig.pa.model.User;
import ie.gtludwig.pa.registration.OnPasswordResetCompleteEvent;
import ie.gtludwig.pa.service.MailService;
import ie.gtludwig.pa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PasswordResetListener implements ApplicationListener<OnPasswordResetCompleteEvent> {

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    @Override
    public void onApplicationEvent(OnPasswordResetCompleteEvent onPasswordResetCompleteEvent) {
        this.confirmPasswordReset(onPasswordResetCompleteEvent);
    }

    private void confirmPasswordReset(final OnPasswordResetCompleteEvent onPasswordResetCompleteEvent) {
        final User user = onPasswordResetCompleteEvent.getUser();
        final String token = UUID.randomUUID().toString();
        userService.createPasswordResetToken(user, token);

        mailService.sendPasswordResetToken(onPasswordResetCompleteEvent, user, token);

        //TODO fix this!
//        mailService.sendPasswordResetToken_HTML(onPasswordResetCompleteEvent, user, token);
    }
}
