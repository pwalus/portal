package portal.analyser.api.configuration;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ApiConfiguration.class)
@TestPropertySource("classpath:api.properties")
public class ApiConfigurationTest {

    @Value("${api.key}")
    private String key;

    @Value("${api.analysisCodes}")
    private String analysisCodes;

    @Test
    public void getKey() {
        assertNotNull(key);
    }

    @Test
    public void getAnalysisCodes() {
        assertTrue(analysisCodes.contains("sentiment"));
        assertTrue(analysisCodes.contains("emotion"));
        assertTrue(analysisCodes.contains("abuse"));
    }
}