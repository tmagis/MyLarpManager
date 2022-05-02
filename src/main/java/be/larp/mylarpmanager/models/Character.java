package be.larp.mylarpmanager.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_user_characters")
    private User player;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "PICTURE_URL")
    private String pictureURL;

    @ManyToMany
    private Collection<Skill> skills;

    @Column(name = "BACKGROUND")
    private String background;

    @Column(name = "AGE")
    private int age;

    @Column(name = "NUMBER_OF_EVENTS_PLAYED")
    private int numberOfEventsPlayed;

    @Column(name = "RACE", nullable = false)
    private String race;

    @Column(name = "IS_ALIVE", nullable = false)
    private boolean isAlive;

    @Column(name = "REASON_OF_DEATH")
    private String reasonOfDeath;

    @Column(name = "CREATION_TIME", nullable = false)
    private LocalDateTime creationTime;

    @Column(name = "LAST_MODIFICATION_TIME", nullable = false)
    private LocalDateTime lastModificationTime;

    public Character() {
        creationTime = LocalDateTime.now();
        lastModificationTime = LocalDateTime.now();
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String picture) {
        this.pictureURL = picture;
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

    public int getNumberOfEventsPlayed() {
        return numberOfEventsPlayed;
    }

    public void setNumberOfEventsPlayed(int numberOfEventsPlayed) {
        this.numberOfEventsPlayed = numberOfEventsPlayed;
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

    @JsonIgnore
    public User getPlayer() {
        return player;
    }

    public void setPlayer(User player) {
        this.player = player;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public Collection<Skill> getSkills() {
        return skills;
    }

    public void setSkills(Collection<Skill> skills) {
        this.skills = skills;
    }

    @Override
    public String toString() {
        return "Character{" +
                "name='" + name + '\'' +
                ", pictureURL='" + pictureURL + '\'' +
                ", background='" + background + '\'' +
                ", age=" + age +
                ", numberOfEventsPlayed=" + numberOfEventsPlayed +
                ", race='" + race + '\'' +
                ", isAlive=" + isAlive +
                ", reasonOfDeath='" + reasonOfDeath + '\'' +
                ", creationTime=" + creationTime +
                ", lastModificationTime=" + lastModificationTime +
                '}';
    }
}
