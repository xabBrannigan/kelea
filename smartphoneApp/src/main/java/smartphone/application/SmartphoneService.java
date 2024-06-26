package smartphone.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import smartphone.domain.Smartphone;
import smartphone.domain.SmartphoneRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class SmartphoneService {

    private final SmartphoneRepository smartphoneRepository;

    public List<Smartphone> getSimilars(String id){

        List<String> similarIds = smartphoneRepository.getIdsOfSimilars(id);
        return smartphoneRepository.getDetails(similarIds);
    };
}
