package be.larp.mylarpmanager.models;

import javax.persistence.*;

@Entity
@Table(name = "TRANSLATED_ITEM")
public class TranslatedLabel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TRANSLATED_ITEM_ID")
    private Long id;

    @Column(name = "EN", nullable = false)
    private String en;

    @Column(name = "FR")
    private String fr;

    @Column(name = "NL")
    private String nl;

    public TranslatedLabel() {
    }

    public String getEn() {
        return en;
    }

    public TranslatedLabel setEn(String en) {
        this.en = en;
        return this;
    }

    public String getFr() {
        return fr;
    }

    public TranslatedLabel setFr(String fr) {
        this.fr = fr;
        return this;
    }

    public String getNl() {
        return nl;
    }

    public TranslatedLabel setNl(String nl) {
        this.nl = nl;
        return this;
    }

    @Override
    public String toString() {
        return "TranslatableLabel{" +
                "en='" + en + '\'' +
                ", fr='" + fr + '\'' +
                ", nl='" + nl + '\'' +
                '}';
    }
}
