package be.larp.mylarpmanager.requests;

import javax.validation.constraints.NotBlank;

public class ProcessDemandRequest implements GenericUuidBasedRequest{

    @NotBlank(message = "Demand uuid is required.")
    private String uuid;

    @NotBlank(message = "Status is required.")
    private String status;

    @NotBlank(message = "ApproverMotivation is required.")
    private String approverMotivation;

    public ProcessDemandRequest(){}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApproverMotivation() {
        return approverMotivation;
    }

    public void setApproverMotivation(String approverMotivation) {
        this.approverMotivation = approverMotivation;
    }

    @Override
    public String getUuid() {
        return this.uuid;
    }

    @Override
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
