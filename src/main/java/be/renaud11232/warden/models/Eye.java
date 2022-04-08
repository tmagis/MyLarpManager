package be.renaud11232.warden.models;

import javax.persistence.*;

@Entity
public class Eye extends UuidModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EYE_ID")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}