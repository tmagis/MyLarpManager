package be.larp.mylarpmanager.security.events;

import be.larp.mylarpmanager.models.uuid.User;

import java.util.Locale;

public class OnPasswordResetEvent extends OnTokenEvent {

    public OnPasswordResetEvent(User user, String baseURL, Locale locale) {
        super(user, baseURL, locale);
    }
}