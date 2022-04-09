package be.renaud11232.warden.models;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Table(name = "CHARACTER")
public class Character extends UuidModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CHARACTER_ID")
    private Long id;

    @Column(name="PLAYER", nullable = false)
    private User player;

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @Column(name = "PICTURE", nullable = true)
    @Lob
    private byte[] picture;

    @OneToMany
    private Collection<Skill> skills;

    @Column(name = "BACKGROUND", nullable = true)
    private String background;

    @Column(name="AGE", nullable = true)
    private int age;

    @Column(name="EVENTS_PLAYED", nullable = true)
    private int eventsPlayed;

    @Column(name="RACE", nullable = false)
    private String race;

    @Column(name="IS_ALIVE", nullable = false)
    private boolean isAlive;

    @Column(name = "REASON_OF_DEATH", nullable=true)
    private String reasonOfDeath;

    @Column(name = "CREATION_TIME", nullable=false)
    private LocalDateTime creationTime;

    @Column(name = "LAST_MODIFICATION_TIME", nullable=false)
    private LocalDateTime lastModificationTime;

    public String getNationName() {
        return name;
    }

    public void setNationName(String nationName) {
        this.name = nationName;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getEventsPlayed() {
        return eventsPlayed;
    }

    public void setEventsPlayed(int eventsPlayed) {
        this.eventsPlayed = eventsPlayed;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public String getReasonOfDeath() {
        return reasonOfDeath;
    }

    public void setReasonOfDeath(String reasonOfDeath) {
        this.reasonOfDeath = reasonOfDeath;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public LocalDateTime getLastModificationTime() {
        return lastModificationTime;
    }

    public void setLastModificationTime(LocalDateTime lastModificationTime) {
        this.lastModificationTime = lastModificationTime;
    }
}
