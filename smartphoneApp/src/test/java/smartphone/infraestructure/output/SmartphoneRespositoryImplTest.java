package smartphone.infraestructure.output;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.*;
import smartphone.domain.Smartphone;
import smartphone.infraestructure.rest.config.properties.UrlProperties;
import smartphone.infraestructure.rest.error.ConnectionException;
import smartphone.infraestructure.rest.error.NotFoundException;
import smartphone.infraestructure.rest.error.TimeoutException;
import smartphone.infraestructure.rest.error.UnknownException;
import smartphone.infraestructure.rest.output.SmartphoneRespositoryImpl;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

public class SmartphoneRespositoryImplTest {

    @Mock
    UrlProperties urlProperties;
    @Mock
    RestTemplate restTemplate;


    @InjectMocks
    SmartphoneRespositoryImpl smartphoneRespository;

    @Before
    public void setUp() throws URISyntaxException {
        MockitoAnnotations.initMocks(this);
    }

    //GetDetails
    @Test
    public void GivenTwoExistingSmartphoneIdsWhenCallingGetDetailsThenTheTwoSmartphonesAreReturned(){
        //Given
        List<String> ids = Arrays.asList("1", "2");

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

        when(urlProperties.getSmartphones_details()).thenReturn("url");
        when(restTemplate.getForObject(urlProperties.getSmartphones_details().concat("1"), Smartphone.class)).thenReturn(one);
        when(restTemplate.getForObject(urlProperties.getSmartphones_details().concat("2"), Smartphone.class)).thenReturn(two);

        //When
        List<Smartphone> result = smartphoneRespository.getDetails(ids);

        //Then
        Assert.assertEquals(result.size(),2);
        assertSmartphone(result.get(0),one);
        assertSmartphone(result.get(1),two);
    }

    @Test
    public void GivenAnEmptyIdsListWhenCallingGetDetailsThenAnEmptyListIsRetourned(){
        //Given
        List<String> ids = new ArrayList<>();

        //When
        List<Smartphone> result = smartphoneRespository.getDetails(ids);

        //Then
        Assert.assertEquals(result.size(),0);
    }

    @Test
    public void GivenANullIdsListWhenCallingGetDetailsThenAnEmptyListIsRetourned(){
        //Given
        List<String> ids = null;

        //When
        List<Smartphone> result = smartphoneRespository.getDetails(ids);

        //Then
        Assert.assertEquals(result.size(),0);
    }

    @Test(expected = NotFoundException.class)
    public void GivenAnApiCallThatThrowsAnHttpClientNotFoundExceptionWhenCallingGetDetailsThenANotFoundExceptionIsThrown(){
        //Given
        List<String> ids = Arrays.asList("1", "2");

        when(urlProperties.getSmartphones_details()).thenReturn("url");
        when(restTemplate.getForObject(urlProperties.getSmartphones_details().concat("1"), Smartphone.class)).thenThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "", null,null, null));

        //When Then
        smartphoneRespository.getDetails(ids);
    }

    @Test(expected = TimeoutException.class)
    public void GivenAnApiCallThatThrowsAnHttpClientTimeoutExceptionWhenCallingGetDetailsThenATimeoutExceptionIsThrown(){
        //Given
        List<String> ids = Arrays.asList("1", "2");

        when(urlProperties.getSmartphones_details()).thenReturn("url");
        when(restTemplate.getForObject(urlProperties.getSmartphones_details().concat("1"), Smartphone.class)).thenThrow(HttpClientErrorException.create(HttpStatus.REQUEST_TIMEOUT, "", null,null, null));

        //When Then
        smartphoneRespository.getDetails(ids);
    }

    @Test(expected = UnknownException.class)
    public void GivenAnApiCallThatThrowsAnotherHttpClientExceptionWhenCallingGetDetailsThenAnUnknownExceptionIsThrown(){
        //Given
        List<String> ids = Arrays.asList("1", "2");

        when(urlProperties.getSmartphones_details()).thenReturn("url");
        when(restTemplate.getForObject(urlProperties.getSmartphones_details().concat("1"), Smartphone.class)).thenThrow(HttpClientErrorException.create(HttpStatus.ALREADY_REPORTED, "", null,null, null));

        //When Then
        smartphoneRespository.getDetails(ids);
    }

    @Test(expected = ConnectionException.class)
    public void GivenAnApiCallThatThrowsAResourceAccessExceptionWhenCallingGetDetailsThenAConnectionExceptionIsThrown(){
        //Given
        List<String> ids = Arrays.asList("1", "2");

        when(urlProperties.getSmartphones_details()).thenReturn("url");
        when(restTemplate.getForObject(urlProperties.getSmartphones_details().concat("1"), Smartphone.class)).thenThrow(new ResourceAccessException(""));

        //When Then
        smartphoneRespository.getDetails(ids);
    }

    @Test(expected = UnknownException.class)
    public void GivenAnApiCallThatThrowsAHttpServerExceptionWhenCallingGetDetailsThenAnUnknownExceptionIsThrown(){
        //Given
        List<String> ids = Arrays.asList("1", "2");

        when(urlProperties.getSmartphones_details()).thenReturn("url");
        when(restTemplate.getForObject(urlProperties.getSmartphones_details().concat("1"), Smartphone.class)).thenThrow(new HttpServerErrorException(HttpStatus.FAILED_DEPENDENCY));

        //When Then
        smartphoneRespository.getDetails(ids);
    }

    @Test(expected = UnknownException.class)
    public void GivenAnApiCallThatThrowsAnUnknownHttpExceptionWhenCallingGetDetailsThenAnUnknownExceptionIsThrown(){
        //Given
        List<String> ids = Arrays.asList("1", "2");

        when(urlProperties.getSmartphones_details()).thenReturn("url");
        when(restTemplate.getForObject(urlProperties.getSmartphones_details().concat("1"), Smartphone.class)).thenThrow(new UnknownHttpStatusCodeException(702,null,null,null,null));

        //When Then
        smartphoneRespository.getDetails(ids);
    }

    private void assertSmartphone(Smartphone a, Smartphone b){
        Assert.assertEquals(a.getBrand(),b.getBrand());
        Assert.assertEquals(a.getId(),b.getId());
        Assert.assertEquals(a.getName(),b.getName());
        Assert.assertEquals(a.getPrice(),b.getPrice());
    }

    //GetSimilars
    @Test
    public void GivenAnSmartphoneIdWithOneSimilarWhenCallingGetIdsOfSimilarsThenAnIdsAreReturned(){
        //Given
        List<String> expectedResult = Arrays.asList("1");
        String[] similars={"1"};

        when(urlProperties.getSimilar_smartphones()).thenReturn("url");
        when(restTemplate.getForObject(urlProperties.getSimilar_smartphones().replace("{id}","id"), String[].class)).thenReturn(similars);

        //When
        List<String> result = smartphoneRespository.getIdsOfSimilars("id");

        //Then
        Assert.assertEquals(result.size(),expectedResult.size());
        expectedResult.stream().forEach(s->Assert.assertTrue(result.contains(s)));
    }

    @Test
    public void GivenAnSmartphoneIdWithTwoSimilarsWhenCallingGetIdsOfSimilarsThenTheTwoIdsAreReturned(){
        //Given
        List<String> expectedResult = Arrays.asList("1", "2");
        String[] similars={"1","2"};

        when(urlProperties.getSimilar_smartphones()).thenReturn("url");
        when(restTemplate.getForObject(urlProperties.getSimilar_smartphones().replace("{id}","id"), String[].class)).thenReturn(similars);

        //When
        List<String> result = smartphoneRespository.getIdsOfSimilars("id");

        //Then
        Assert.assertEquals(result.size(),expectedResult.size());
        expectedResult.stream().forEach(s->Assert.assertTrue(result.contains(s)));
    }

    @Test
    public void GivenAnSmartphoneIdWithNoSimilarsWhenCallingGetIdsOfSimilarsThenAnEmptyListIsReturned(){
        //Given
        String[] similars=new String[0];

        when(urlProperties.getSimilar_smartphones()).thenReturn("url");
        when(restTemplate.getForObject(urlProperties.getSimilar_smartphones().replace("{id}","id"), String[].class)).thenReturn(similars);

        //When
        List<String> result = smartphoneRespository.getIdsOfSimilars("id");

        //Then
        Assert.assertEquals(result.size(),0);
    }

    @Test(expected = NotFoundException.class)
    public void GivenAnApiCallThatThrowsAnHttpClientNotFoundExceptionWhenCallingGetIdsOfSimilarsThenANotFoundExceptionIsThrown(){
        //Given
        when(urlProperties.getSimilar_smartphones()).thenReturn("url");
        when(restTemplate.getForObject(urlProperties.getSimilar_smartphones().replace("{id}","id"), String[].class)).thenThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "", null,null, null));

        //When Then
        smartphoneRespository.getIdsOfSimilars("id");
    }

    @Test(expected = TimeoutException.class)
    public void GivenAnApiCallThatThrowsAnHttpClientTimeoutExceptionWhenCallingGetIdsOfSimilarsThenATimeoutExceptionIsThrown(){
        //Given
        when(urlProperties.getSimilar_smartphones()).thenReturn("url");
        when(restTemplate.getForObject(urlProperties.getSimilar_smartphones().replace("{id}","id"), String[].class)).thenThrow(HttpClientErrorException.create(HttpStatus.REQUEST_TIMEOUT, "", null,null, null));

        //When Then
        smartphoneRespository.getIdsOfSimilars("id");
    }

    @Test(expected = UnknownException.class)
    public void GivenAnApiCallThatThrowsAnotherHttpClientExceptionWhenCallingGetIdsOfSimilarsThenAnUnknownExceptionIsThrown(){
        //Given
        when(urlProperties.getSimilar_smartphones()).thenReturn("url");
        when(restTemplate.getForObject(urlProperties.getSimilar_smartphones().replace("{id}","id"), String[].class)).thenThrow(HttpClientErrorException.create(HttpStatus.ALREADY_REPORTED, "", null,null, null));

        //When Then
        smartphoneRespository.getIdsOfSimilars("id");
    }

    @Test(expected = ConnectionException.class)
    public void GivenAnApiCallThatThrowsAResourceAccessExceptionWhenCallingGetIdsOfSimilarsThenAConnectionExceptionIsThrown(){
        //Given
        when(urlProperties.getSimilar_smartphones()).thenReturn("url");
        when(restTemplate.getForObject(urlProperties.getSimilar_smartphones().replace("{id}","id"), String[].class)).thenThrow(new ResourceAccessException(""));

        //When Then
        smartphoneRespository.getIdsOfSimilars("id");
    }

    @Test(expected = UnknownException.class)
    public void GivenAnApiCallThatThrowsAHttpServerExceptionWhenCallingGetIdsOfSimilarsThenAnUnknownExceptionIsThrown(){
        //Given
        when(urlProperties.getSimilar_smartphones()).thenReturn("url");
        when(restTemplate.getForObject(urlProperties.getSimilar_smartphones().replace("{id}","id"), String[].class)).thenThrow(new HttpServerErrorException(HttpStatus.FAILED_DEPENDENCY));

        //When Then
        smartphoneRespository.getIdsOfSimilars("id");
    }

    @Test(expected = UnknownException.class)
    public void GivenAnApiCallThatThrowsAnUnknownHttpExceptionWhenCallingGetIdsOfSimilarsThenAnUnknownExceptionIsThrown(){
        //Given
        when(urlProperties.getSimilar_smartphones()).thenReturn("url");
        when(restTemplate.getForObject(urlProperties.getSimilar_smartphones().replace("{id}","id"), String[].class)).thenThrow(new UnknownHttpStatusCodeException(702,null,null,null,null));

        //When Then
        smartphoneRespository.getIdsOfSimilars("id");
    }
}
