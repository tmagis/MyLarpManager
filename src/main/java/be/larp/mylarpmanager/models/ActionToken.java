package be.larp.mylarpmanager.models;

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

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(LocalDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }
}