package be.larp.mylarpmanager.models;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "TRANSLATABLE_ITEM")
public class TranslatableLabel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TRANSLATABLE_ITEM_ID")
    private Long id;

    @Column(name = "EN", nullable = false)
    private String en;

    @Column(name = "FR")
    private String fr;

    @Column(name = "NL")
    private String nl;

    public TranslatableLabel() {
    }

    public String getEn() {
        return en;
    }

    public TranslatableLabel setEn(String en) {
        this.en = en;
        return this;
    }

    public String getFr() {
        return fr;
    }

    public TranslatableLabel setFr(String fr) {
        this.fr = fr;
        return this;
    }

    public String getNl() {
        return nl;
    }

    public TranslatableLabel setNl(String nl) {
        this.nl = nl;
        return this;
    }
}
