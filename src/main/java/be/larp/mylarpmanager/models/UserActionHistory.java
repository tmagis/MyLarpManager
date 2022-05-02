package be.larp.mylarpmanager.models;


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

    @Column(name = "ACTION", nullable = false)
    private String action;

    @Column(name = "ACTION_TIME", nullable = false)
    private LocalDateTime actionTime;

    public UserActionHistory() {
        actionTime = LocalDateTime.now();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public LocalDateTime getActionTime() {
        return actionTime;
    }

    public void setActionTime(LocalDateTime actionTime) {
        this.actionTime = actionTime;
    }
}