package portal.analyser.api.configuration;

import java.util.*;
import lombok.*;
import org.springframework.boot.context.properties.*;
import org.springframework.context.annotation.*;

@Configuration
@ConfigurationProperties(prefix = "api")
@PropertySource("classpath:api.properties")
@Data
public class ApiConfiguration {

    private String key;

    private List<String> analysisCodes;

}
