package portal.statistic;

import javax.persistence.*;
import javax.persistence.criteria.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import portal.domain.*;
import portal.domain.analysis.*;

@Component
public class PrintStatistic {

    private final EntityManager entityManager;

    @Autowired
    public PrintStatistic(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void printFor(Long projectId, String analysisMethod) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Statistic> query = criteriaBuilder.createQuery(Statistic.class);

        Root<CommentAnalysisItem> root = query.from(CommentAnalysisItem.class);
        Join<CommentAnalysisItem, CommentAnalysis> commentsAnalysisJoin = root
            .join(CommentAnalysisItem_.commentAnalysis);
        Join<CommentAnalysis, Comment> commentsJoin = commentsAnalysisJoin
            .join(CommentAnalysis_.comment);
        Join<Comment, Issue> issueJoin = commentsJoin.join(Comment_.issue);
        Join<Issue, Project> projectJoin = issueJoin.join(Issue_.project);

        query.groupBy(root.get(CommentAnalysisItem_.name));
        query.where(
            criteriaBuilder.equal(projectJoin.get(Project_.id), projectId),
            criteriaBuilder.equal(commentsAnalysisJoin.get(CommentAnalysis_.code), analysisMethod)
        );

        Expression<Double> avg = criteriaBuilder.avg(root.get(CommentAnalysisItem_.VALUE));
        query.multiselect(root.get(CommentAnalysisItem_.NAME), avg);
    }

}
