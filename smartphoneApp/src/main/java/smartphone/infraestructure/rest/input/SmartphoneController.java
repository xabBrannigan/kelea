package smartphone.infraestructure.rest.input;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import smartphone.application.SmartphoneService;
import smartphone.domain.Smartphone;
import java.util.List;

@AllArgsConstructor
@RestController
public class SmartphoneController {

    private SmartphoneService smartphoneService;

    @GetMapping("/smartphone/{id}/similar")
    public ResponseEntity<List<Smartphone>> findSimilarSmartphones(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(smartphoneService.getSimilars(id));
    }
}
