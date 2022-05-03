package be.larp.mylarpmanager.models.uuid;

import be.larp.mylarpmanager.models.Status;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "JOIN_NATION_DEMAND")
public class JoinNationDemand extends UuidModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "JOIN_NATION_DEMAND_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User candidate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Nation nation;

    @Column(name="MOTIVATION")
    private String motivation;

    @ManyToOne(fetch = FetchType.LAZY)
    private User approver;

    //Only if Refused. Not visible by the candidate.
    @Column(name="APPROVER_MOTIVATION")
    private String approverMotivation;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private Status status;

    @Column(name = "REQUEST_TIME", nullable = false)
    private LocalDateTime requestTime;

    @Column(name = "PROCESSING_TIME")
    private LocalDateTime processingTime;

    public JoinNationDemand() {
        requestTime = LocalDateTime.now();
        setUuid();
    }

    public User getCandidate() {
        return candidate;
    }

    public void setCandidate(User candidate) {
        this.candidate = candidate;
    }

    public Nation getNation() {
        return nation;
    }

    public void setNation(Nation nation) {
        this.nation = nation;
    }

    public String getMotivation() {
        return motivation;
    }

    public void setMotivation(String motivation) {
        this.motivation = motivation;
    }

    public User getApprover() {
        return approver;
    }

    public void setApprover(User approver) {
        this.approver = approver;
    }

    public String getApproverMotivation() {
        return approverMotivation;
    }

    public void setApproverMotivation(String approverMotivation) {
        this.approverMotivation = approverMotivation;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(LocalDateTime requestTime) {
        this.requestTime = requestTime;
    }

    public LocalDateTime getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(LocalDateTime processingTime) {
        this.processingTime = processingTime;
    }
}
