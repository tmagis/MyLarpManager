package be.larp.mylarpmanager.repositories;

import be.larp.mylarpmanager.models.uuid.JoinNationDemand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JoinNationDemandRepository extends JpaRepository<JoinNationDemand, Long>, UuidRepository<JoinNationDemand> {

}
