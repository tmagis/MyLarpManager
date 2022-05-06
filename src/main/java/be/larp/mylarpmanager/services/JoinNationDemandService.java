package be.larp.mylarpmanager.services;

import be.larp.mylarpmanager.models.uuid.JoinNationDemand;
import be.larp.mylarpmanager.repositories.JoinNationDemandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;

@Service
@Transactional
public class JoinNationDemandService {

    @Autowired
    private JoinNationDemandRepository joinNationDemandRepository;

    public JoinNationDemand getJoinNationDemandByUuid(String uuid) {
        return joinNationDemandRepository.findByUuid(uuid)
                .orElseThrow(() -> new NoSuchElementException("JoinNationDemand with uuid " + uuid + " not found."));
    }

    public void delete(JoinNationDemand joinNationDemand) {
        joinNationDemandRepository.delete(joinNationDemand);
    }

    public void save(JoinNationDemand joinNationDemand) {
        joinNationDemandRepository.saveAndFlush(joinNationDemand);
    }
}
