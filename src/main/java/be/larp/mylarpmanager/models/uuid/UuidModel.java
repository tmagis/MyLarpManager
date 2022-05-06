package be.larp.mylarpmanager.models.uuid;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Objects;
import java.util.UUID;

@MappedSuperclass
public abstract class UuidModel {

    @Column(name = "UUID", nullable = false, unique = true)
    private String uuid;

    public String getUuid() {
        return uuid;
    }

    public UuidModel setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public UuidModel setUuid(){
        this.uuid = getRandomUuid();
        return this;
    }

    private String getRandomUuid() {
        return UUID.randomUUID().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UuidModel)) return false;
        UuidModel uuidModel = (UuidModel) o;
        return uuid.equals(uuidModel.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}