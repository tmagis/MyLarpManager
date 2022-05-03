package be.larp.mylarpmanager.models.uuid;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "POINT_HISTORY")
public class PointHistory extends UuidModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POINT_HISTORY_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_eventparticipation")
    private EventParticipation eventParticipation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_character_awardedto")
    private Character awardedTo;

    @Column(name = "POINTS")
    private int points;

    @Column(name="GAINED_ON")
    private LocalDateTime gainedOn;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_user_awardedby")
    private User awardedBy;

    @Column(name="REASON", columnDefinition = "LONGTEXT")
    private String reason;

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public LocalDateTime getGainedOn() {
        return gainedOn;
    }

    public void setGainedOn(LocalDateTime gainedOn) {
        this.gainedOn = gainedOn;
    }

    public User getAwardedBy() {
        return awardedBy;
    }

    public void setAwardedBy(User awardedBy) {
        this.awardedBy = awardedBy;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public EventParticipation getEventParticipation() {
        return eventParticipation;
    }

    public void setEventParticipation(EventParticipation eventParticipation) {
        this.eventParticipation = eventParticipation;
    }

    public Character getAwardedTo() {
        return awardedTo;
    }

    public void setAwardedTo(Character awardedTo) {
        this.awardedTo = awardedTo;
    }

    public PointHistory() {
        gainedOn = LocalDateTime.now();
        setUuid();
    }
}
