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

    public Map<String, Map<String, String>> analyse(List<Comment> comments) throws Exception {
        App app = new App(apiConfiguration.getKey());
        JSONArray jsonArray = toJsonArray(comments);
        String response = app.sentiment_batch(jsonArray);
        logger.info(response);

        Map<String, Map<String, String>> responseMap = new HashMap<>();
        responseMap.put("sentiment", parseResponse(response));

        return responseMap;
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> parseResponse(String response) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(response);
        JSONArray jsonArray = (JSONArray) jsonObject.get("sentiment");
        Iterator iterator = jsonArray.iterator();

        Map<String, String> objectMap = new HashMap<>();
        while (iterator.hasNext()) {
            JSONObject itemArray = (JSONObject) iterator.next();
            itemArray.forEach((o, o2) -> objectMap.put(o.toString(), o2.toString()));
        }

        return objectMap;
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
