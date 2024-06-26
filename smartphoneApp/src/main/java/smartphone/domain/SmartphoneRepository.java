package smartphone.domain;

import java.util.List;

public interface SmartphoneRepository {
    List<String> getIdsOfSimilars(String id);
    List<Smartphone> getDetails(List<String> ids);
}
