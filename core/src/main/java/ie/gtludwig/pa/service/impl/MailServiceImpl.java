package ie.gtludwig.pa.service.impl;

import ie.gtludwig.pa.model.User;
import ie.gtludwig.pa.registration.OnInvitationCompleteEvent;
import ie.gtludwig.pa.registration.OnPasswordResetCompleteEvent;
import ie.gtludwig.pa.registration.OnRegistrationCompleteEvent;
import ie.gtludwig.pa.service.MailContentBuilderService;
import ie.gtludwig.pa.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service(value = "mailServiceImpl")
public class MailServiceImpl implements MailService {

    private static final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private Environment environment;

    @Autowired
    private MessageSource messageSource;


    @Autowired
    private MailContentBuilderService mailContentBuilderService;


    protected void sendMail(SimpleMailMessage mailMessage) {
        try {
            javaMailSender.send(mailMessage);
        } catch (MailException e) {
            // runtime exception; compiler will not force you to handle it
        }
    }

    protected void prepareAndSendMail(Map<String, String> emailMap) {

        try {
            InternetAddress[] parsed;
            try {
                parsed = InternetAddress.parse(emailMap.get("to"));
            } catch (AddressException e) {
                throw new IllegalArgumentException("Not valid email: " + emailMap.get("to)"), e);
            }

            MimeMessage mailMessage = javaMailSender.createMimeMessage();
            mailMessage.setSubject(emailMap.get("subject"), "UTF-8");

            MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true, "UTF-8");
            helper.setFrom(emailMap.get("from"));
            helper.setTo(parsed);
            helper.setText(mailContentBuilderService.buildHTMLTemplate(emailMap.get("text")), true);

            javaMailSender.send(mailMessage);
        } catch (MessagingException ex) {
            throw new RuntimeException(ex);
        }
    }

    protected String buildConfirmRegistrationButton(String label, String confirmationUrl) {
        StringBuilder content = new StringBuilder();
        content.append("<div class=\"jumbotron\">");
        content.append("<div class=\"container-fluid\">");
        content.append("<div class=\"form-group row justify-content-md-center justify-content-lg-center justify-content-xl-center\">");
        content.append("<label>" + label + "</label> \n\r");
        content.append("<br />");
        content.append("<a href=\"" + confirmationUrl + "\">");

        content.append("<button class=\"btn btn-primary\"> Click here to confirm </button>");

        content.append("</a>");

        content.append("</div>");
        content.append("</div>");
        content.append("</div>");

        return content.toString();
    }


    @Override
    public void sendNewUserRegistrationToken(OnRegistrationCompleteEvent onRegistrationCompleteEvent, User user, String token) {
        Locale locale = Locale.US;

//        final String recipientAddress = user.getEmail();
        final String subject = messageSource.getMessage("mail.registrationConfirmationSubject", null, locale);
        final String confirmationUrl = onRegistrationCompleteEvent.getAppUrl() + "/admin/user/registration/confirm?token=" + token;
        final String message = messageSource.getMessage("mail.registrationConfirmationLabel", new Object[]{user.getEmail()}, locale);
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setSubject(subject);
        email.setText(message + " \r\n" + confirmationUrl);
        email.setFrom(environment.getProperty("support.email"));
        sendMail(email);
    }

    @Override
    public void sendNewUserRegistrationToken_HTML(OnRegistrationCompleteEvent onRegistrationCompleteEvent, User user, String token) {
        Locale locale = Locale.US;

        final String subject = messageSource.getMessage("mail.registrationConfirmationSubject", null, locale);
        final String label = messageSource.getMessage("mail.registrationConfirmationLabel", new Object[]{user.getEmail()}, locale);
        final String confirmationUrl = onRegistrationCompleteEvent.getAppUrl() + "/admin/user/registration/confirm?token=" + token;

        final String content = buildConfirmRegistrationButton(label, confirmationUrl);

        Map<String, String> emailMap = new HashMap<>();
        emailMap.put("to", user.getEmail());
        emailMap.put("from", environment.getProperty("support.email"));
        emailMap.put("subject", subject);
        emailMap.put("text", content);

        prepareAndSendMail(emailMap);
    }

    @Override
    public void sendPasswordResetToken(OnPasswordResetCompleteEvent onPasswordResetCompleteEvent, User user, String token) {
        Locale locale = Locale.US;

        final String subject = messageSource.getMessage("mail.passwordResetSubject", null, locale);
        final String label = messageSource.getMessage("mail.passwordResetLabel", null, locale);
        final String resetUrl = onPasswordResetCompleteEvent.getAppUrl() + "/admin/user/registration/resetPassword?token=" + token;

        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setSubject(subject);
        email.setText(label + " \r\n" + resetUrl);
        email.setFrom(environment.getProperty("support.email"));
        sendMail(email);
    }

    @Override
    public void sendPasswordResetToken_HTML(OnPasswordResetCompleteEvent onPasswordResetCompleteEvent, User user, String token) {

    }

    @Override
    public void sendInvitedUserRegistrationToken(OnInvitationCompleteEvent onInvitationCompleteEvent, User user, String token) {
        Locale locale = Locale.US;

        final String subject = messageSource.getMessage("mail.invitationRegistrationSubject", null, locale);
        final String label = messageSource.getMessage("mail.invitationRegistrationLabel", null, locale);
        final String resetUrl = onInvitationCompleteEvent.getAppUrl() + "/admin/user/registration/registerInvited?token=" + token;

        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setSubject(subject);
        email.setText(label + " \r\n" + resetUrl);
        email.setFrom(environment.getProperty("support.email"));
        sendMail(email);
    }

    @Override
    public void sendInvitedUserRegistrationToken_HTML(OnInvitationCompleteEvent onInvitationCompleteEvent, User user, String token) {

    }
}
