package smartphone.infraestructure.rest.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import smartphone.application.SmartphoneService;
import smartphone.domain.SmartphoneRepository;
import smartphone.infraestructure.rest.config.properties.ExceptionsProperties;
import smartphone.infraestructure.rest.config.properties.UrlProperties;
import smartphone.infraestructure.rest.error.ErrorHandler;
import smartphone.infraestructure.rest.input.SmartphoneController;
import smartphone.infraestructure.rest.output.SmartphoneRespositoryImpl;

@Configuration
@RequiredArgsConstructor
public class infraestructureConfig {
    
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    private final UrlProperties urlProperties;

    @Bean
    public SmartphoneRepository smartphoneRepository(RestTemplate restTemplate, UrlProperties urlProperties){
        return new SmartphoneRespositoryImpl(restTemplate, urlProperties);
    }

}
