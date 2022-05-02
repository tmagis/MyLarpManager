package be.larp.mylarpmanager.security.listeners;

import be.larp.mylarpmanager.models.User;
import be.larp.mylarpmanager.models.ActionType;
import be.larp.mylarpmanager.security.events.OnPasswordResetEvent;
import be.larp.mylarpmanager.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class PasswordResetEventListener implements
        ApplicationListener<OnPasswordResetEvent> {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private MessageSource messages;

    @Autowired
    private JavaMailSender mailSender;


    @Override
    public void onApplicationEvent(OnPasswordResetEvent event) {
        this.passwordReset(event);
    }

    private void passwordReset(OnPasswordResetEvent event) {User user = event.getUser();
        String token = userDetailsService.createActionToken(user, ActionType.PASSWORD_RESET);
        String recipientAddress = user.getEmail();
        String subject = "Password reset";
        String confirmationUrl = event.getBaseURL()+"/setpassword?token=" + token;
        String message = messages.getMessage("message.resetPassword", null,event.getLocale());
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + "\r\n" +  confirmationUrl);
        mailSender.send(email);
    }
}