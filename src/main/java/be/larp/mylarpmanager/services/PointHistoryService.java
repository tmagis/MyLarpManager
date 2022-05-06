package be.larp.mylarpmanager.services;

import be.larp.mylarpmanager.models.uuid.PointHistory;
import be.larp.mylarpmanager.models.uuid.User;
import be.larp.mylarpmanager.repositories.PointHistoryRepository;
import be.larp.mylarpmanager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class PointHistoryService {

    @Autowired
    private PointHistoryRepository pointHistoryRepository;

    public PointHistory getPointHistoryByUuid(String uuid) {
        return pointHistoryRepository.findByUuid(uuid)
                .orElseThrow(() -> new NoSuchElementException("PointHistory with uuid " + uuid + " not found."));
    }

    public void delete(PointHistory pointHistory) {
        pointHistoryRepository.delete(pointHistory);
    }

    public List<PointHistory> getAll(){
        return pointHistoryRepository.findAll();
    }

    public void save(PointHistory pointHistory) {
        pointHistoryRepository.saveAndFlush(pointHistory);
    }
}
