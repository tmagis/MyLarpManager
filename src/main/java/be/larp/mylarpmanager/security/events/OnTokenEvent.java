package be.larp.mylarpmanager.security.events;

import be.larp.mylarpmanager.models.User;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

public class OnTokenEvent extends ApplicationEvent {

    private User user;

    private Locale locale;

    private String baseURL;

    public OnTokenEvent(User user, String baseURL, Locale locale) {
        super(user);
        this.locale = locale;
        this.baseURL = baseURL;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }
}
