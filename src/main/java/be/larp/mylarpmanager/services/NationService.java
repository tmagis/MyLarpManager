package be.larp.mylarpmanager.services;

import be.larp.mylarpmanager.models.uuid.Nation;
import be.larp.mylarpmanager.repositories.NationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class NationService {

    @Autowired
    private NationRepository nationRepository;

    public Nation getNationByUuid(String uuid) {
        return nationRepository.findByUuid(uuid)
                .orElseThrow(() -> new NoSuchElementException("Nation with uuid " + uuid + " not found."));
    }

    public void delete(Nation nation) {
        nationRepository.delete(nation);
    }

    public void save(Nation nation) {
        nationRepository.saveAndFlush(nation);
    }

    public List<Nation> getAll() {
        return nationRepository.findAll();
    }
}
