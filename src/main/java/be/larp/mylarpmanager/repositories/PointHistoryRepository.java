package be.larp.mylarpmanager.repositories;

import be.larp.mylarpmanager.models.uuid.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {

    Optional<PointHistory> findByUuid(String uuid);

}
