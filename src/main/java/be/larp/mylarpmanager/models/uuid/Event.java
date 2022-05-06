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

    public Event setYearTI(int yearTI) {
        this.yearTI = yearTI;
        return this;
    }

    public TranslatedItem getName() {
        return name;
    }

    public Event setName(TranslatedItem name) {
        this.name = name;
        return this;
    }

    public TranslatedItem getDescription() {
        return description;
    }

    public Event setDescription(TranslatedItem description) {
        this.description = description;
        return this;
    }

    public LocalDateTime getDateFrom() {
        return dateFrom;
    }

    public Event setDateFrom(LocalDateTime dateFrom) {
        this.dateFrom = dateFrom;
        return this;
    }

    public LocalDateTime getDateTo() {
        return dateTo;
    }

    public Event setDateTo(LocalDateTime dateTo) {
        this.dateTo = dateTo;
        return this;
    }

    public LocalDateTime getDeadlineLogisticPack() {
        return deadlineLogisticPack;
    }

    public Event setDeadlineLogisticPack(LocalDateTime deadlineLogisticPack) {
        this.deadlineLogisticPack = deadlineLogisticPack;
        return this;
    }

    public boolean isConcluded() {
        return concluded;
    }

    public Event setConcluded(boolean concluded) {
        this.concluded = concluded;
        return this;
    }

    public List<EventParticipation> getEventParticipationList() {
        return eventParticipationList;
    }

    public Event setEventParticipationList(List<EventParticipation> eventParticipationList) {
        this.eventParticipationList = eventParticipationList;
        return this;
    }
}
