package be.larp.mylarpmanager.repositories;

import be.larp.mylarpmanager.models.UserActionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserActionHistoryRepository extends JpaRepository<UserActionHistory, Long> {

}
