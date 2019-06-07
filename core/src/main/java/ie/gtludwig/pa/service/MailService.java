package ie.gtludwig.pa.service;

import ie.gtludwig.pa.model.User;
import ie.gtludwig.pa.registration.OnInvitationCompleteEvent;
import ie.gtludwig.pa.registration.OnPasswordResetCompleteEvent;
import ie.gtludwig.pa.registration.OnRegistrationCompleteEvent;

public interface MailService {

    void sendNewUserRegistrationToken(OnRegistrationCompleteEvent onRegistrationCompleteEvent, User user, String token);

    void sendNewUserRegistrationToken_HTML(OnRegistrationCompleteEvent onRegistrationCompleteEvent, User user, String token);

    void sendPasswordResetToken(OnPasswordResetCompleteEvent onPasswordResetCompleteEvent, User user, String token);

    void sendPasswordResetToken_HTML(OnPasswordResetCompleteEvent onPasswordResetCompleteEvent, User user, String token);

    void sendInvitedUserRegistrationToken(OnInvitationCompleteEvent onInvitationCompleteEvent, User user, String token);

    void sendInvitedUserRegistrationToken_HTML(OnInvitationCompleteEvent onInvitationCompleteEvent, User user, String token);
}
