package smartphone.infraestructure.rest.output;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.*;
import smartphone.domain.Smartphone;
import smartphone.domain.SmartphoneRepository;
import smartphone.infraestructure.rest.config.properties.UrlProperties;
import smartphone.infraestructure.rest.error.NotFoundException;
import smartphone.infraestructure.rest.error.TimeoutException;
import smartphone.infraestructure.rest.error.UnknownException;
import smartphone.infraestructure.rest.error.ConnectionException;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class SmartphoneRespositoryImpl implements SmartphoneRepository {

    private final UrlProperties urlProperties;
    RestTemplate restTemplate;

    public SmartphoneRespositoryImpl(RestTemplate restTemplate, UrlProperties urlProperties) {
        this.restTemplate = restTemplate;
        this.urlProperties = urlProperties;
    }

    @Override
    public List<String> getIdsOfSimilars(String id) {
        return restCallToGetSimilarIds(id);
    }

    private List<String> restCallToGetSimilarIds(String id){

        try {

            String[] responseIdsArray = restTemplate.getForObject(urlProperties.getSimilar_smartphones().replace("{id}",id), String[].class);
            return responseEntityToIdsList(responseIdsArray);

        }catch (Exception e) {
            manageExceptions(e,id);
            log.error(e.getMessage());
            throw new UnknownException(e.getMessage());
        }
    }

    private List<String> responseEntityToIdsList(String[] responseIdsArray){
        List<String> responseIds = new ArrayList<>();
        log.info("Numero de similares encontrados: ".concat(String.valueOf(responseIdsArray.length)));
        if(Objects.nonNull(responseIdsArray)){
            log.info("Similares encontrados: ".concat(Arrays.toString(responseIdsArray)));
            responseIds = Arrays.asList(responseIdsArray);
        }
        return responseIds;
    }



    @Override
    public List<Smartphone> getDetails(List<String> ids) {
        return CollectionUtils.emptyIfNull(ids).stream().map(this::restCallToGetDetails).collect(Collectors.toList());
    }

    private Smartphone restCallToGetDetails(String id){
        try {

            return restTemplate.getForObject(urlProperties.getSmartphones_details().concat(id), Smartphone.class);

        }catch (Exception e) {
            manageExceptions(e,id);
            log.error(e.getMessage());
            throw new UnknownException(e.getMessage());
        }
    }

    private void manageExceptions(Exception e, String id){
        if(e instanceof ResourceAccessException){
            log.error(e.getMessage());
            throw new ConnectionException(e.getMessage());
        }
        if (e instanceof HttpClientErrorException) {
            manageClientException((HttpClientErrorException)e,id);
        }
        if (e instanceof HttpServerErrorException | e instanceof UnknownHttpStatusCodeException){
            log.error(e.getMessage());
            throw new UnknownException(e.getMessage());
        }
    }

    private void manageClientException(HttpClientErrorException e, String id){
        if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
            throw new NotFoundException(id);
        }
        if (e.getStatusCode().equals(HttpStatus.REQUEST_TIMEOUT)) {
            log.error(e.getMessage());
            throw new TimeoutException(e.getMessage());
        }
    }
}
