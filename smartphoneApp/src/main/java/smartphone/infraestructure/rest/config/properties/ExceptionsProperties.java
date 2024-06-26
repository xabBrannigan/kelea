package smartphone.infraestructure.rest.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "infraestructure.exception")
public class ExceptionsProperties {
    private String not_found;
    private String timeout;
    private String runtime;
    private String connection;
}
