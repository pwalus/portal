package portal.analyser;

import java.util.concurrent.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import portal.domain.*;

@Component
public class CommentAnalyser {

    private final Logger logger = LoggerFactory.getLogger(CommentAnalyser.class);

    @Autowired
    private ThreadBridge threadBridge;

    public void analyse() {
        logger.info("Waiting for comments to analyze...");

        try {
            while (true) {
                Comment comment = threadBridge.getCommentsQueue().poll(10, TimeUnit.SECONDS);
                logger.info(comment.getId().toString());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        logger.info("End comments analyzing...");
    }
}
