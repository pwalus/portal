package portal.analyser.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.junit4.SpringRunner;
import portal.domain.Comment;
import portal.domain.Issue;
import portal.domain.Project;
import portal.repository.CommentAnalysisItemRepository;
import portal.repository.CommentAnalysisRepository;
import portal.repository.CommentRepository;
import portal.repository.IssueRepository;
import portal.repository.ProjectRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional
public class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentAnalysisRepository commentAnalysisRepository;

    @Autowired
    private CommentAnalysisItemRepository commentAnalysisItemRepository;

    @Test
    public void givenRepository_whenAutowired_thenNotNull() {
        assertNotNull(projectRepository);
        assertNotNull(issueRepository);
        assertNotNull(commentRepository);
        assertNotNull(commentAnalysisRepository);
        assertNotNull(commentAnalysisItemRepository);
    }

    @Test
    public void givenProjectRepository_whenNewProject_thenSaved() {
        Project project = createProject();

        Project savedProject = projectRepository.getOne(project.getId());
        assertEquals("test/test", savedProject.getRepositoryId());
        assertEquals("http://github.com/test/test", savedProject.getUrl());
    }

    @NotNull
    private Project createProject() {
        Project project = new Project();
        project.setRepositoryId("test/test");
        project.setUrl("http://github.com/test/test");
        projectRepository.save(project);
        return project;
    }

    @Test
    public void givenIssueRepository_whenNewIssue_thenSaved() {
        Issue issue = createIssue();

        Issue savedIssue = issueRepository.getOne(issue.getId());
        assertEquals("Test", savedIssue.getTitle());
        assertEquals("http://github.com/test/test/issue/1", savedIssue.getUrl());
        assertEquals("1", savedIssue.getIssueId());
    }

    @NotNull
    private Issue createIssue() {
        Issue issue = new Issue();
        issue.setTitle("Test");
        issue.setUrl("http://github.com/test/test/issue/1");
        issue.setIssueId("1");
        Project project = createProject();
        issue.setProject(project);
        issueRepository.save(issue);

        return issue;
    }

    @Test
    public void givenCommentRepository_whenNewComment_thenSaved() {
        Comment comment = new Comment();
        comment.setCommentId("12");
        comment.setContent("Test content");

        Issue issue = createIssue();
        comment.setIssue(issue);

        commentRepository.save(comment);

        Comment savedComment = commentRepository.getOne(comment.getId());
        assertEquals("12", savedComment.getCommentId());
        assertEquals("Test content", savedComment.getContent());
    }


}
