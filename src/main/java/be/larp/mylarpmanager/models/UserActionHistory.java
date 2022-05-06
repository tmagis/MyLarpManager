package be.larp.mylarpmanager.models;


import be.larp.mylarpmanager.models.uuid.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "USER_ACTION_HISTORY")
public class UserActionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ACTION_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_action_history_user")
    private User user;

    @Column(name = "ACTION", columnDefinition = "LONGTEXT", nullable = false)
    private String action;

    @Column(name = "ACTION_TIME", nullable = false)
    private LocalDateTime actionTime;

    public UserActionHistory() {
        actionTime = LocalDateTime.now();
    }

    public User getUser() {
        return user;
    }

    public UserActionHistory setUser(User user) {
        this.user = user;
        return this;
    }

    public String getAction() {
        return action;
    }

    public UserActionHistory setAction(String action) {
        this.action = action;
        return this;
    }

    public LocalDateTime getActionTime() {
        return actionTime;
    }

    public UserActionHistory setActionTime(LocalDateTime actionTime) {
        this.actionTime = actionTime;
        return this;
    }
}