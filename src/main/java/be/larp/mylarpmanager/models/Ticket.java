package be.larp.mylarpmanager.models;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Ticket extends UuidModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TICKET_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_ticket")
    private User player;

    @Column(name = "EXTERNAL_ID", nullable = false, unique = true)
    private String externalId;

    @Column(name = "CODE", nullable = false, unique = true)
    private String code;

    @Column(name = "LINKED_EMAIL", nullable = false, unique = true)
    private String linkedEmail;

    @Column(name = "PURCHASE_DATE", nullable = false)
    private LocalDateTime purchaseDate;

    @Column(name = "IS_PAYED", nullable = false)
    private String isPayed;

    @Column(name = "IS_CANCELLED", nullable = false)
    private String isCancelled;

    public User getPlayer() {
        return player;
    }

    public void setPlayer(User player) {
        this.player = player;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getIsPayed() {
        return isPayed;
    }

    public void setIsPayed(String isPayed) {
        this.isPayed = isPayed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsCancelled() {
        return isCancelled;
    }

    public void setIsCancelled(String isCancelled) {
        this.isCancelled = isCancelled;
    }

    public String getLinkedEmail() {
        return linkedEmail;
    }

    public void setLinkedEmail(String linkedEmail) {
        this.linkedEmail = linkedEmail;
    }
}