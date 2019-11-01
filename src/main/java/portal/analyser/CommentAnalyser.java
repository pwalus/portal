package portal.analyser;

import java.util.*;
import java.util.Map.*;
import java.util.concurrent.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import portal.analyser.api.*;
import portal.domain.*;
import portal.domain.analysis.*;
import portal.repository.*;
import portal.thread.*;

@Component
public class CommentAnalyser {

    public static final int BATCH_NUMBER = 5;

    private final Logger logger = LoggerFactory.getLogger(CommentAnalyser.class);

    private ThreadBridge threadBridge;

    private CommentAnalysisRepository commentAnalysisRepository;

    private CommentAnalysisItemRepository commentAnalysisItemRepository;

    private ApiService apiService;

    @Autowired
    public CommentAnalyser(
        ThreadBridge threadBridge,
        CommentAnalysisRepository commentAnalysisRepository,
        CommentAnalysisItemRepository commentAnalysisItemRepository,
        ApiService apiService
    ) {
        this.threadBridge = threadBridge;
        this.commentAnalysisRepository = commentAnalysisRepository;
        this.commentAnalysisItemRepository = commentAnalysisItemRepository;
        this.apiService = apiService;
    }

    public void analyse() {
        logger.info("Waiting for comments to analyze...");
        consumeQueue();
        logger.info("End comments analyzing...");
    }

    private void consumeQueue() {
        List<Comment> comments = new ArrayList<>();

        try {
            while (true) {
                comments.add(threadBridge.getCommentsQueue().poll(10, TimeUnit.SECONDS));
                analyseBatch(comments, false);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        analyseBatch(comments, true);
    }

    public void analyseBatch(List<Comment> comments, boolean flush) {
        if (comments.size() > BATCH_NUMBER || flush) {
            try {
                save(comments, apiService.analyse(comments));
                System.exit(1);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    private void save(
        List<Comment> comments,
        Map<String, List<Map<String, String>>> analyseResponse
    ) {
        for (Map.Entry<String, List<Map<String, String>>> entry : analyseResponse.entrySet()) {
            for (int i = 0; i < comments.size(); i++) {
                CommentAnalysis commentAnalysis = createCommentAnalysis(comments.get(i), entry.getKey());
                createCommentAnalysisItems(entry.getValue().get(i), commentAnalysis);
            }
        }
    }

    private CommentAnalysis createCommentAnalysis(Comment comment, String analysisCode) {
        CommentAnalysis commentAnalysis = new CommentAnalysis();
        commentAnalysis.setComment(comment);
        commentAnalysis.setAnalysisCode(analysisCode);
        return commentAnalysisRepository.save(commentAnalysis);
    }

    private void createCommentAnalysisItems(
        Map<String, String> commentResponse,
        CommentAnalysis commentAnalysis
    ) {
        for (Entry<String, String> item : commentResponse.entrySet()) {
            CommentAnalysisItem commentAnalysisItem = new CommentAnalysisItem();
            commentAnalysisItem.setCommentAnalysis(commentAnalysis);
            commentAnalysisItem.setName(item.getKey());
            commentAnalysisItem.setValue(item.getValue());
            commentAnalysisItemRepository.save(commentAnalysisItem);
        }
    }

}
