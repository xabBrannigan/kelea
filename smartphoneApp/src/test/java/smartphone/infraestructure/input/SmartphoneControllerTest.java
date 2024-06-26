package smartphone.infraestructure.input;

//import smartphone.input.controller.SimilarPricedController;
//import smartphone.input.model.SimilarSmartphone;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import smartphone.application.SmartphoneService;
import smartphone.domain.Smartphone;
import smartphone.infraestructure.rest.config.properties.UrlProperties;
import smartphone.infraestructure.rest.error.NotFoundException;
import smartphone.infraestructure.rest.input.SmartphoneController;
import smartphone.infraestructure.rest.output.SmartphoneRespositoryImpl;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;


public class SmartphoneControllerTest {

    @Mock
    SmartphoneService smartphoneService;

    @InjectMocks
    SmartphoneController smartphoneController;

    @Before
    public void setUp() throws URISyntaxException {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void GivenAnIdWithResultsWhenCallingFindSimilarSmartphonesThenAListOfSimilarsIsReturned() {

        //Given
        String id = "id";
        Smartphone one = new Smartphone();
            one.setBrand("Samsung");
            one.setId("1");
            one.setName("name");
            one.setPrice(999.9f);
        Smartphone two = new Smartphone();
            one.setBrand("Apple");
            one.setId("1");
            one.setName("name2");
            one.setPrice(999.0f);
        List<Smartphone> similars = Arrays.asList(one,two);
        when(smartphoneService.getSimilars(id)).thenReturn(similars);
        //When
        ResponseEntity<List<Smartphone>> responseEntity = smartphoneController.findSimilarSmartphones(id);

        //Then
        Assertions.assertThat(HttpStatus.OK).isEqualTo( responseEntity.getStatusCode());
        Assertions.assertThat(similars).isEqualTo(responseEntity.getBody());
    }

}
