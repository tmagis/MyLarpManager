package be.larp.mylarpmanager.security.events;

import be.larp.mylarpmanager.models.User;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

public class OnPasswordResetEvent extends OnTokenEvent {

    public OnPasswordResetEvent(User user, String baseURL, Locale locale) {
        super(user, baseURL, locale);
    }
}