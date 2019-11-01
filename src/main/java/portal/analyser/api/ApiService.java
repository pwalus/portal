package portal.analyser.api;

import com.paralleldots.paralleldots.*;
import java.util.*;
import org.json.simple.*;
import org.json.simple.parser.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import portal.analyser.*;
import portal.analyser.api.configuration.*;
import portal.domain.*;

@Service
public class ApiService {

    private final Logger logger = LoggerFactory.getLogger(CommentAnalyser.class);

    @Autowired
    private ApiConfiguration apiConfiguration;

    public Map<String, List<String>> analyse(List<Comment> comments) throws Exception {
        App app = new App(apiConfiguration.getKey());
        JSONArray jsonArray = toJsonArray(comments);
        String response = app.sentiment_batch(jsonArray);
        logger.info(response);

        Map<String, List<String>> responseMap = new HashMap<>();
        responseMap.put("sentiment", parseResponse(response));

        return responseMap;
    }

    private List<String> parseResponse(String response) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(response);
        JSONArray jsonArray = (JSONArray) jsonObject.get("sentiment");
        Iterator iterator = jsonArray.iterator();

        List<String> jsonContent = new ArrayList<>();
        while (iterator.hasNext()) {
            jsonContent.add(iterator.next().toString());
        }

        return jsonContent;
    }


    @SuppressWarnings("unchecked")
    private JSONArray toJsonArray(List<Comment> comments) {
        JSONArray jsonArray = new JSONArray();
        comments.stream()
            .map(Comment::getContent)
            .forEach(jsonArray::add);

        return jsonArray;
    }
}
