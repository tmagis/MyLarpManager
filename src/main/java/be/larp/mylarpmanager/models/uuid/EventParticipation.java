package be.larp.mylarpmanager.models.uuid;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "EVENT_PARTICIPATION")
public class EventParticipation extends UuidModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EVENT_PARTICIPATION_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_character_chosen_one")
    private Character chosenOne;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_nation")
    private Nation nation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_event")
    private Event event;

    @Column(name="SUMMARY")
    private String summary;

    @OneToMany(mappedBy = "eventParticipation")
    private List<PointHistory> pointHistories;

    public EventParticipation() {
        setUuid();
    }

    public Character getChosenOne() {
        return chosenOne;
    }

    public EventParticipation setChosenOne(Character chosenOne) {
        this.chosenOne = chosenOne;
        return this;
    }

    public Nation getNation() {
        return nation;
    }

    public EventParticipation setNation(Nation nation) {
        this.nation = nation;
        return this;
    }

    public Event getEvent() {
        return event;
    }

    public EventParticipation setEvent(Event event) {
        this.event = event;
        return this;
    }

    public String getSummary() {
        return summary;
    }

    public EventParticipation setSummary(String summary) {
        this.summary = summary;
        return this;
    }

    public List<PointHistory> getPointHistories() {
        return pointHistories;
    }

    public EventParticipation setPointHistories(List<PointHistory> pointHistories) {
        this.pointHistories = pointHistories;
        return this;
    }
}
