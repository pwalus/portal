package portal.analyser;

import java.util.concurrent.*;
import org.springframework.stereotype.*;
import portal.domain.*;

@Component
public class ThreadBridge {

    private final BlockingQueue<Comment> commentsQueue = new LinkedBlockingQueue<>();

    public BlockingQueue<Comment> getCommentsQueue() {
        return commentsQueue;
    }
}
