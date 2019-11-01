package portal.analyser.api;

import java.util.*;
import org.json.simple.*;
import org.json.simple.parser.*;
import org.junit.*;
import org.springframework.boot.test.context.*;

@SpringBootTest
public class ApiServiceTest {

    private ApiService apiService = new ApiService();

    @Test
    public void analyse() throws Exception {
        String test = "{\"sentiment\":[{\"negative\":0.901,\"neutral\":0.083,\"positive\":0.016},{\"negative\":0.855,\"neutral\":0.124,\"positive\":0.02},{\"negative\":0.052,\"neutral\":0.699,\"positive\":0.25},{\"negative\":0.717,\"neutral\":0.216,\"positive\":0.067},{\"negative\":0.219,\"neutral\":0.435,\"positive\":0.346},{},{\"negative\":0.946,\"neutral\":0.048,\"positive\":0.005},{\"negative\":0.389,\"neutral\":0.487,\"positive\":0.124},{\"negative\":0.199,\"neutral\":0.663,\"positive\":0.138},{\"negative\":0.325,\"neutral\":0.248,\"positive\":0.427},{\"negative\":0.855,\"neutral\":0.117,\"positive\":0.027}]}";
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(test);
        JSONArray jsonArray = (JSONArray) jsonObject.get("sentiment");
        Iterator iterator = jsonArray.iterator();

        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}