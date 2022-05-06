package be.larp.mylarpmanager.models.uuid;

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

    public Ticket() {
        setUuid();
    }

    public User getPlayer() {
        return player;
    }

    public Ticket setPlayer(User player) {
        this.player = player;
        return this;
    }

    public String getExternalId() {
        return externalId;
    }

    public Ticket setExternalId(String externalId) {
        this.externalId = externalId;
        return this;
    }

    public String getCode() {
        return code;
    }

    public Ticket setCode(String code) {
        this.code = code;
        return this;
    }

    public String getLinkedEmail() {
        return linkedEmail;
    }

    public Ticket setLinkedEmail(String linkedEmail) {
        this.linkedEmail = linkedEmail;
        return this;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public Ticket setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
        return this;
    }

    public String getIsPayed() {
        return isPayed;
    }

    public Ticket setIsPayed(String isPayed) {
        this.isPayed = isPayed;
        return this;
    }

    public String getIsCancelled() {
        return isCancelled;
    }

    public Ticket setIsCancelled(String isCancelled) {
        this.isCancelled = isCancelled;
        return this;
    }
}