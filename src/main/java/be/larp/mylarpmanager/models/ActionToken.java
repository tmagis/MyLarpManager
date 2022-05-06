package be.larp.mylarpmanager.models;

import be.larp.mylarpmanager.models.uuid.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ACTION_TOKEN")
public class ActionToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ACTION_TOKEN_ID")
    private Long id;

    @Column(name = "TOKEN", nullable = false)
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(name = "ACTION_TYPE", nullable = false)
    private ActionType actionType;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "USER_ID")
    private User user;

    @Column(name = "EXPIRATION_TIME", nullable = false)
    private LocalDateTime expirationTime;

    public ActionToken() {
    }

    public String getToken() {
        return token;
    }

    public ActionToken setToken(String token) {
        this.token = token;
        return this;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public ActionToken setActionType(ActionType actionType) {
        this.actionType = actionType;
        return this;
    }

    public User getUser() {
        return user;
    }

    public ActionToken setUser(User user) {
        this.user = user;
        return this;
    }

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    public ActionToken setExpirationTime(LocalDateTime expirationTime) {
        this.expirationTime = expirationTime;
        return this;
    }
}