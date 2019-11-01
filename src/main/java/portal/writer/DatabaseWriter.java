package portal.writer;

import com.vdurmont.emoji.*;
import java.util.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import portal.domain.*;
import portal.repository.*;
import portal.thread.*;

@Service
public class DatabaseWriter implements Writer {

    private Logger logger = LoggerFactory.getLogger(DatabaseWriter.class);

    private ProjectRepository projectRepository;

    private IssueRepository issueRepository;

    private CommentRepository commentRepository;

    private ThreadBridge threadBridge;

    private Map<String, Project> projectCache = new HashMap<>();

    private Map<String, Issue> issueCache = new HashMap<>();

    @Autowired
    public DatabaseWriter(
        ProjectRepository projectRepository,
        IssueRepository issueRepository,
        CommentRepository commentRepository,
        ThreadBridge threadBridge
    ) {
        this.projectRepository = projectRepository;
        this.issueRepository = issueRepository;
        this.commentRepository = commentRepository;
        this.threadBridge = threadBridge;
    }

    @Override
    public void writeProject(String repositoryId, String repositoryUrl) {
        Project project = projectRepository.findByRepositoryId(repositoryId).orElse(new Project());
        project.setRepositoryId(repositoryId);
        project.setUrl(repositoryUrl);
        projectRepository.save(project);
    }

    @Override
    public void writeIssue(String issueId, String repositoryId, String issueUrl, String title) {
        Issue issue = issueRepository.findByIssueId(issueId).orElse(new Issue());
        issue.setIssueId(issueId);
        issue.setUrl(issueUrl);
        issue.setTitle(EmojiParser.removeAllEmojis(title));
        issue.setProject(getProject(repositoryId));
        issueRepository.save(issue);
    }

    private Project getProject(String repositoryId) throws NoSuchElementException {
        if (!projectCache.containsKey(repositoryId)) {
            Optional<Project> optional = projectRepository.findByRepositoryId(repositoryId);
            optional.orElseThrow(NoSuchElementException::new);
            projectCache.put(repositoryId, optional.get());
        }

        return projectCache.get(repositoryId);
    }

    @Override
    public void writeComments(String issueId, Map<String, String> comments) {
        comments.entrySet().stream().map(comment -> {
            Comment commentEntity = getComment(comment.getKey());
            commentEntity.setCommentId(comment.getKey());
            commentEntity.setContent(EmojiParser.removeAllEmojis(comment.getValue()));
            commentEntity.setIssue(getIssue(issueId));

            return commentEntity;
        }).filter(
            comment -> !comment.getContent().isEmpty()
        ).forEach(
            commentEntity -> {
                logger.info("Saving: " + commentEntity.getCommentId());
                commentRepository.save(commentEntity);
                threadBridge.getCommentsQueue().add(commentEntity);
            }
        );
    }

    private Issue getIssue(String issueId) throws NoSuchElementException {
        if (!issueCache.containsKey(issueId)) {
            Optional<Issue> optional = issueRepository.findByIssueId(issueId);
            optional.orElseThrow(NoSuchElementException::new);
            issueCache.put(issueId, optional.get());
        }

        return issueCache.get(issueId);
    }

    private Comment getComment(String commentId) {
        Optional<Comment> optional = commentRepository.findByCommentId(commentId);
        return optional.orElseGet(Comment::new);
    }
}
