package be.larp.mylarpmanager.models.uuid;

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

    @Column(name = "PICTURE_URL", columnDefinition = "LONGTEXT")
    private String pictureURL;

    @ManyToMany
    private Collection<Skill> skills;

    @Column(name = "BACKGROUND", columnDefinition = "LONGTEXT")
    private String background;

    @Column(name = "AGE")
    private int age;

    @Column(name = "NUMBER_OF_EVENTS_PLAYED")
    private int numberOfEventsPlayed;

    @Column(name = "RACE", nullable = false)
    private String race;

    @Column(name = "IS_ALIVE", nullable = false)
    private boolean isAlive;

    @Column(name = "IS_NPC", nullable = false)
    private boolean isNPC;

    @Column(name = "REASON_OF_DEATH")
    private String reasonOfDeath;

    @Column(name = "CREATION_TIME", nullable = false)
    private LocalDateTime creationTime;

    @Column(name = "LAST_MODIFICATION_TIME", nullable = false)
    private LocalDateTime lastModificationTime;

    public Character() {
        isAlive = true;
        isNPC = false;
        creationTime = LocalDateTime.now();
        lastModificationTime = LocalDateTime.now();
        setUuid();
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public Character setPictureURL(String picture) {
        this.pictureURL = picture;
        return this;
    }

    public String getBackground() {
        return background;
    }

    public Character setBackground(String background) {
        this.background = background;
        return this;
    }

    public int getAge() {
        return age;
    }

    public Character setAge(int age) {
        this.age = age;
        return this;
    }

    public int getNumberOfEventsPlayed() {
        return numberOfEventsPlayed;
    }

    public Character setNumberOfEventsPlayed(int numberOfEventsPlayed) {
        this.numberOfEventsPlayed = numberOfEventsPlayed;
        return this;
    }

    public String getRace() {
        return race;
    }

    public Character setRace(String race) {
        this.race = race;
        return this;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public Character setAlive(boolean alive) {
        isAlive = alive;
        return this;
    }

    public String getReasonOfDeath() {
        return reasonOfDeath;
    }

    public Character setReasonOfDeath(String reasonOfDeath) {
        this.reasonOfDeath = reasonOfDeath;
        return this;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public Character setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
        return this;
    }

    public LocalDateTime getLastModificationTime() {
        return lastModificationTime;
    }

    public Character setLastModificationTime(LocalDateTime lastModificationTime) {
        this.lastModificationTime = lastModificationTime;
        return this;
    }

    @JsonIgnore
    public User getPlayer() {
        return player;
    }

    public Character setPlayer(User player) {
        this.player = player;
        return this;
    }

    public String getName() {
        return name;
    }

    public Character setName(String name) {
        this.name = name;
        return this;
    }

    public boolean isNPC() {
        return isNPC;
    }

    public Character setNPC(boolean npc) {
        isNPC = npc;
        return this;
    }

    @JsonIgnore
    public Collection<Skill> getSkills() {
        return skills;
    }

    public Character setSkills(Collection<Skill> skills) {
        this.skills = skills;
        return this;
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
