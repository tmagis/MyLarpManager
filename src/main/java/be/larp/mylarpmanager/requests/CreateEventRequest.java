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

    public void setName(TranslatedItem name) {
        this.name = name;
    }

    public int getYearTI() {
        return yearTI;
    }

    public void setYearTI(int yearTI) {
        this.yearTI = yearTI;
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
}
