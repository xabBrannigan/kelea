package smartphone.infraestructure.rest.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "infraestructure.endpoint")
public class UrlProperties {
    private String similar_smartphones;
    private String smartphones_details;
}
