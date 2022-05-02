package be.larp.mylarpmanager.security.listeners;

import be.larp.mylarpmanager.models.User;
import be.larp.mylarpmanager.models.ActionType;
import be.larp.mylarpmanager.security.events.OnRegistrationCompleteEvent;
import be.larp.mylarpmanager.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class RegistrationEventListener implements
        ApplicationListener<OnRegistrationCompleteEvent> {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private MessageSource messages;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }
    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = userDetailsService.createActionToken(user, ActionType.VERIFY_EMAIL);
        String recipientAddress = user.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl = event.getBaseURL()+"/confirm?token=" + token;
        String message = messages.getMessage("message.confirmRegistration", null,event.getLocale());
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + "\r\n" + confirmationUrl);
        mailSender.send(email);
    }
}