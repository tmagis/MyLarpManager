package be.larp.mylarpmanager.repositories;

import java.util.Optional;

public interface UuidRepository<T>{
        Optional<T> findByUuid(String uuid);
}
