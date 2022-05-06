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

    public PointHistory setPoints(int points) {
        this.points = points;
        return this;
    }

    public LocalDateTime getGainedOn() {
        return gainedOn;
    }

    public PointHistory setGainedOn(LocalDateTime gainedOn) {
        this.gainedOn = gainedOn;
        return this;
    }

    public User getAwardedBy() {
        return awardedBy;
    }

    public PointHistory setAwardedBy(User awardedBy) {
        this.awardedBy = awardedBy;
        return this;
    }

    public String getReason() {
        return reason;
    }

    public PointHistory setReason(String reason) {
        this.reason = reason;
        return this;
    }

    public EventParticipation getEventParticipation() {
        return eventParticipation;
    }

    public PointHistory setEventParticipation(EventParticipation eventParticipation) {
        this.eventParticipation = eventParticipation;
        return this;
    }

    public Character getAwardedTo() {
        return awardedTo;
    }

    public PointHistory setAwardedTo(Character awardedTo) {
        this.awardedTo = awardedTo;
        return this;
    }

    public PointHistory() {
        gainedOn = LocalDateTime.now();
        setUuid();
    }
}
