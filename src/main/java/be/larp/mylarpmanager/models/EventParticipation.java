package be.larp.mylarpmanager.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    @JoinColumn(name = "fk_character_choosen_one")
    private Character choosenOne;

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
    }

    public Character getChoosenOne() {
        return choosenOne;
    }

    public void setChoosenOne(Character choosenOne) {
        this.choosenOne = choosenOne;
    }

    public Nation getNation() {
        return nation;
    }

    public void setNation(Nation nation) {
        this.nation = nation;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<PointHistory> getPointHistories() {
        return pointHistories;
    }

    public void setPointHistories(List<PointHistory> pointHistories) {
        this.pointHistories = pointHistories;
    }
}
