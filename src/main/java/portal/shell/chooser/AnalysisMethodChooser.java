package portal.shell.chooser;

import java.util.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import portal.analyser.api.configuration.*;
import portal.shell.*;

@Component
public class AnalysisMethodChooser {

    private final ApiConfiguration apiConfiguration;

    private final InputReader inputReader;

    @Autowired
    public AnalysisMethodChooser(ApiConfiguration apiConfiguration, InputReader inputReader) {
        this.apiConfiguration = apiConfiguration;
        this.inputReader = inputReader;
    }

    public String chooseMethod() {
        int i = 0;
        Map<String, String> options = new HashMap<>();
        for (String code : apiConfiguration.getAnalysisCodes()) {
            options.put(String.valueOf(i++), code);
        }

        String value = inputReader
            .selectFromList(
                "Choose analysis method",
                "Please enter one of the [] values",
                options,
                true,
                null
            );

        return options.get(value);
    }
}
