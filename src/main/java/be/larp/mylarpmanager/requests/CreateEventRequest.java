package be.larp.mylarpmanager.requests;


import be.larp.mylarpmanager.models.TranslatedItem;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class CreateEventRequest {

    @NotBlank(message = "Event name is required.")
    private TranslatedItem name;

    private int yearTI;
    private TranslatedItem description;

    @NotNull(message = "Event begin date is required")
    private LocalDateTime dateFrom;

    @NotNull(message = "Event end date is required")
    private LocalDateTime dateTo;

    private LocalDateTime deadlineLogisticPack;

    private boolean concluded;

    public CreateEventRequest() {
    }

    public TranslatedItem getName() {
        return name;
    }

    public CreateEventRequest setName(TranslatedItem name) {
        this.name = name;
        return this;
    }

    public int getYearTI() {
        return yearTI;
    }

    public CreateEventRequest setYearTI(int yearTI) {
        this.yearTI = yearTI;
        return this;
    }

    public TranslatedItem getDescription() {
        return description;
    }

    public CreateEventRequest setDescription(TranslatedItem description) {
        this.description = description;
        return this;
    }

    public LocalDateTime getDateFrom() {
        return dateFrom;
    }

    public CreateEventRequest setDateFrom(LocalDateTime dateFrom) {
        this.dateFrom = dateFrom;
        return this;
    }

    public LocalDateTime getDateTo() {
        return dateTo;
    }

    public CreateEventRequest setDateTo(LocalDateTime dateTo) {
        this.dateTo = dateTo;
        return this;
    }

    public LocalDateTime getDeadlineLogisticPack() {
        return deadlineLogisticPack;
    }

    public CreateEventRequest setDeadlineLogisticPack(LocalDateTime deadlineLogisticPack) {
        this.deadlineLogisticPack = deadlineLogisticPack;
        return this;
    }

    public boolean isConcluded() {
        return concluded;
    }

    public CreateEventRequest setConcluded(boolean concluded) {
        this.concluded = concluded;
        return this;
    }
}
