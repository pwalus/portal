package portal.analyser.api;

import com.paralleldots.paralleldots.*;
import java.lang.reflect.*;
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

    private ApiConfiguration apiConfiguration;

    @Autowired
    public ApiService(ApiConfiguration apiConfiguration) {
        this.apiConfiguration = apiConfiguration;
    }

    public Map<String, List<Map<String, String>>> analyse(List<Comment> comments) {
        logger.info("Analysing comments...");
        ServiceInvoker serviceInvoker = new ServiceInvoker();
        Map<String, List<Map<String, String>>> responseMap = new HashMap<>();
        for (String analysisCode : apiConfiguration.getAnalysisCodes()) {
            try {
                List<Map<String, String>> response = serviceInvoker.invoke(analysisCode, comments);
                responseMap.put(analysisCode, response);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        return responseMap;
    }

    private class ServiceInvoker {

        private List<Map<String, String>> invoke(String analysisCode, List<Comment> comments) throws Exception {
            App app = new App(apiConfiguration.getKey());
            JSONArray jsonArray = toJsonArray(comments);

            Method method = app.getClass().getDeclaredMethod(analysisCode + "_batch", JSONArray.class);
            logger.info("Invoking " + analysisCode + "_batch method...");
            String response = (String) method.invoke(app, jsonArray);

            if(response.contains("You have exceeded the rate limit")){
                logger.info("You have exceeded the rate limit of daily api usage");
                return new ArrayList<>();
            }

            logger.info(response);

            return parseResponse(response, analysisCode);
        }

        @SuppressWarnings("unchecked")
        private List<Map<String, String>> parseResponse(String response, String analysisCode)
            throws ParseException {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(response);
            JSONArray jsonArray = (JSONArray) jsonObject.get(analysisCode);
            Iterator iterator = jsonArray.iterator();

            List<Map<String, String>> objectList = new ArrayList<>();
            while (iterator.hasNext()) {
                Map<String, String> objectMap = new HashMap<>();
                JSONObject itemArray = (JSONObject) iterator.next();
                itemArray.forEach((o, o2) -> objectMap.put(o.toString(), o2.toString()));
                objectList.add(objectMap);
            }

            return objectList;
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
}
