package portal.analyser.api.configuration;

import lombok.*;
import org.springframework.boot.context.properties.*;
import org.springframework.context.annotation.*;

@Configuration
@ConfigurationProperties(prefix = "api")
@PropertySource("classpath:api.properties")
@Data
public class ApiConfiguration {

    private String key;

}
