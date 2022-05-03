package be.larp.mylarpmanager.models.uuid;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

@MappedSuperclass
public abstract class UuidModel {

    @Column(name = "UUID", nullable = false, unique = true)
    private String uuid;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setUuid(){
        this.uuid = getRandomUuid();
    }

    private String getRandomUuid() {
        return UUID.randomUUID().toString();
    }
}