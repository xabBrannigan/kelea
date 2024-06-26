package smartphone.infraestructure.input;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.*;
import smartphone.infraestructure.rest.config.properties.UrlProperties;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SmartphoneControllerIT {

    @Autowired
    private MockMvc mvc;

    @MockBean
    RestTemplate restTemplate;
    @MockBean
    UrlProperties urlProperties;

    @Test
    public void GivenANonRespondingSmartphonesApiWhenCallingTheEndpointThenAnErrorResultIsReturned() throws Exception {
        //Given
        when(urlProperties.getSimilar_smartphones()).thenReturn("url");
        when(restTemplate.getForObject(urlProperties.getSimilar_smartphones().replace("{id}","1"), String[].class)).thenThrow(new ResourceAccessException(""));

        //When Then
        mvc.perform(MockMvcRequestBuilders
                        .get("/smartphone/1/similar")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(equalTo("Se ha producido un error al conectar con la api externa de obtencion de smartphones")));
    }

    @Test
    public void GivenANotFoundExceptionWhenCallingTheEndpointThenAnErrorResultIsReturned() throws Exception {
        //Given
        when(urlProperties.getSimilar_smartphones()).thenReturn("url");
        when(restTemplate.getForObject(urlProperties.getSimilar_smartphones().replace("{id}","1"), String[].class)).thenThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "", null,null, null));

        //When Then
        mvc.perform(MockMvcRequestBuilders
                        .get("/smartphone/1/similar")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(equalTo("No se ha encontrado el smartphone con id 1")));
    }

    @Test
    public void GivenANHttpClientTimeoutExceptionWhenCallingTheEndpointThenAnErrorResultIsReturned() throws Exception {
        //Given
        when(urlProperties.getSimilar_smartphones()).thenReturn("url");
        when(restTemplate.getForObject(urlProperties.getSimilar_smartphones().replace("{id}","1"), String[].class)).thenThrow(HttpClientErrorException.create(HttpStatus.REQUEST_TIMEOUT, "", null,null, null));

        //When Then
        mvc.perform(MockMvcRequestBuilders
                        .get("/smartphone/1/similar")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(equalTo("Se ha producido un timeout al conectar con la api externa de obtencion de smartphones")));
    }

    @Test
    public void GivenAnotherHttpClientExceptionWhenCallingTheEndpointThenAnErrorResultIsReturned() throws Exception {
        //Given
        when(urlProperties.getSimilar_smartphones()).thenReturn("url");
        when(restTemplate.getForObject(urlProperties.getSimilar_smartphones().replace("{id}","1"), String[].class)).thenThrow(HttpClientErrorException.create(HttpStatus.ALREADY_REPORTED, "", null,null, null));

        //When Then
        mvc.perform(MockMvcRequestBuilders
                        .get("/smartphone/1/similar")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(equalTo("Se ha producido un error desconocido ")));
    }

    @Test
    public void GivenAnHttpServerExceptionWhenCallingTheEndpointThenAnErrorResultIsReturned() throws Exception {
        //Given
        when(urlProperties.getSimilar_smartphones()).thenReturn("url");
        when(restTemplate.getForObject(urlProperties.getSimilar_smartphones().replace("{id}","1"), String[].class)).thenThrow(new HttpServerErrorException(HttpStatus.FAILED_DEPENDENCY));

        //When Then
        mvc.perform(MockMvcRequestBuilders
                        .get("/smartphone/1/similar")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(equalTo("Se ha producido un error desconocido ")));
    }

    @Test
    public void GivenAnUnknwonHttpExceptionWhenCallingTheEndpointThenAnErrorResultIsReturned() throws Exception {
        //Given
        when(urlProperties.getSimilar_smartphones()).thenReturn("url");
        when(restTemplate.getForObject(urlProperties.getSimilar_smartphones().replace("{id}","1"), String[].class)).thenThrow(new UnknownHttpStatusCodeException(702,null,null,null,null));

        //When Then
        mvc.perform(MockMvcRequestBuilders
                        .get("/smartphone/1/similar")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(equalTo("Se ha producido un error desconocido ")));
    }
}
