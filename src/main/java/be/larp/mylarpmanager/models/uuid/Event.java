package be.larp.mylarpmanager.models.uuid;

import be.larp.mylarpmanager.models.TranslatedItem;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "EVENT")
public class Event extends UuidModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EVENT_ID")
    private Long id;

    @Column(name="YEAR_TI")
    private int yearTI;

    @OneToOne(optional = false, cascade = CascadeType.PERSIST)
    private TranslatedItem name;

    @OneToOne(cascade = CascadeType.PERSIST)
    private TranslatedItem description;

    @Column(name="DATE_FROM", nullable = false)
    private LocalDateTime dateFrom;

    @Column(name="DATE_TO", nullable = false)
    private LocalDateTime dateTo;

    @Column(name="DEADLINE_LOGISTIC_PACK")
    private LocalDateTime deadlineLogisticPack;

    @Column(name = "CONCLUDED")
    private boolean concluded;

    @OneToMany(mappedBy = "event")
    private List<EventParticipation> eventParticipationList;

    public Event() {
        setUuid();
    }

    public int getYearTI() {
        return yearTI;
    }

    public void setYearTI(int yearTI) {
        this.yearTI = yearTI;
    }

    public TranslatedItem getName() {
        return name;
    }

    public void setName(TranslatedItem name) {
        this.name = name;
    }

    public TranslatedItem getDescription() {
        return description;
    }

    public void setDescription(TranslatedItem description) {
        this.description = description;
    }

    public LocalDateTime getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDateTime dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDateTime getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDateTime dateTo) {
        this.dateTo = dateTo;
    }

    public LocalDateTime getDeadlineLogisticPack() {
        return deadlineLogisticPack;
    }

    public void setDeadlineLogisticPack(LocalDateTime deadlineLogisticPack) {
        this.deadlineLogisticPack = deadlineLogisticPack;
    }

    public boolean isConcluded() {
        return concluded;
    }

    public void setConcluded(boolean concluded) {
        this.concluded = concluded;
    }

    public List<EventParticipation> getEventParticipationList() {
        return eventParticipationList;
    }

    public void setEventParticipationList(List<EventParticipation> eventParticipationList) {
        this.eventParticipationList = eventParticipationList;
    }
}
