package be.larp.mylarpmanager.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    }


}
