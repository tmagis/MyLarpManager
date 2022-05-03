package be.larp.mylarpmanager.models;

import javax.persistence.*;

@Entity
@Table(name = "TRANSLATED_ITEM")
public class TranslatedItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TRANSLATED_ITEM_ID")
    private Long id;

    @Column(name = "EN", columnDefinition = "LONGTEXT")
    private String en;

    @Column(name = "FR", nullable = false, columnDefinition = "LONGTEXT")
    private String fr;

    @Column(name = "NL", columnDefinition = "LONGTEXT")
    private String nl;

    public TranslatedItem() {
    }

    public String getEn() {
        return en;
    }

    public TranslatedItem setEn(String en) {
        this.en = en;
        return this;
    }

    public String getFr() {
        return fr;
    }

    public TranslatedItem setFr(String fr) {
        this.fr = fr;
        return this;
    }

    public String getNl() {
        return nl;
    }

    public TranslatedItem setNl(String nl) {
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
