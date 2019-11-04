package portal.analyser;

import java.util.*;
import java.util.Map.*;
import java.util.concurrent.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import portal.analyser.api.*;
import portal.analyser.api.configuration.*;
import portal.domain.*;
import portal.domain.analysis.*;
import portal.repository.*;
import portal.thread.*;

@Component
public class CommentAnalyser {

    public static final int BATCH_NUMBER = 10;

    private final Logger logger = LoggerFactory.getLogger(CommentAnalyser.class);

    private ThreadBridge threadBridge;

    private CommentAnalysisRepository commentAnalysisRepository;

    private CommentAnalysisItemRepository commentAnalysisItemRepository;

    private ApiService apiService;

    private ApiConfiguration apiConfiguration;

    private List<Comment> comments = new ArrayList<>();

    @Autowired
    public CommentAnalyser(
        ThreadBridge threadBridge,
        CommentAnalysisRepository commentAnalysisRepository,
        CommentAnalysisItemRepository commentAnalysisItemRepository,
        ApiService apiService,
        ApiConfiguration apiConfiguration
    ) {
        this.threadBridge = threadBridge;
        this.commentAnalysisRepository = commentAnalysisRepository;
        this.commentAnalysisItemRepository = commentAnalysisItemRepository;
        this.apiService = apiService;
        this.apiConfiguration = apiConfiguration;
    }

    public void analyse() {
        logger.info("Waiting for comments to analyze...");
        consumeQueue();
        logger.info("End comments analyzing...");
    }

    private void consumeQueue() {
        try {
            while (true) {
                Comment comment = threadBridge.getCommentsQueue().poll(30, TimeUnit.SECONDS);
                if (comment == null) {
                    logger.info("Empty queue...");
                    break;
                }

                if (isNotAnalysed(comment)) {
                    logger.info("Comment: " + comment.getCommentId() + " not analysed...");
                    comments.add(comment);
                }

                analyseBatch(false);
            }
        } catch (InterruptedException e) {
            logger.info(e.getMessage());
        }

        analyseBatch(true);
    }

    private boolean isNotAnalysed(Comment comment) {
        return commentAnalysisRepository.countByCommentId(comment.getId()) < 1;
    }

    public void analyseBatch(boolean flush) {
        if (!comments.isEmpty() && (comments.size() > BATCH_NUMBER || flush)) {
            try {
                for (String analysisCode : apiConfiguration.getAnalysisCodes()) {
                    save(apiService.analyse(comments, analysisCode), analysisCode);
                }
                comments = new ArrayList<>();
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    private void save(List<Map<String, String>> analyseResponse, String analysisCode) {
        if (analyseResponse.isEmpty()) {
            System.exit(1);
        }

        for (int i = 0; i < comments.size(); i++) {
            CommentAnalysis commentAnalysis = createCommentAnalysis(comments.get(i), analysisCode);
            createCommentAnalysisItems(analyseResponse.get(i), commentAnalysis);
        }
    }

    private CommentAnalysis createCommentAnalysis(Comment comment, String analysisCode) {
        CommentAnalysis commentAnalysis = new CommentAnalysis();
        commentAnalysis.setComment(comment);
        commentAnalysis.setCode(analysisCode);
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
