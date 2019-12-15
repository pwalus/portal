package portal.statistic;

import java.text.NumberFormat;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.table.ArrayTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.TableBuilder;
import org.springframework.shell.table.TableModel;
import org.springframework.stereotype.Component;
import portal.domain.Comment;
import portal.domain.Comment_;
import portal.domain.Issue;
import portal.domain.Issue_;
import portal.domain.Project;
import portal.domain.Project_;
import portal.domain.analysis.CommentAnalysis;
import portal.domain.analysis.CommentAnalysisItem;
import portal.domain.analysis.CommentAnalysisItem_;
import portal.domain.analysis.CommentAnalysis_;
import portal.shell.ShellHelper;

@Component
public class PrintStatistic {

    private final EntityManager entityManager;

    private final ShellHelper shellHelper;

    @Autowired
    public PrintStatistic(EntityManager entityManager, ShellHelper shellHelper) {
        this.entityManager = entityManager;
        this.shellHelper = shellHelper;
    }

    public void printFor(Long projectId, String analysisMethod) {
        List<Statistic> statistics = getStatisticList(projectId, analysisMethod);
        String[][] array = new String[statistics.size()][2];

        NumberFormat numberFormat = NumberFormat.getPercentInstance();
        for (int i = 0; i < statistics.size(); i++) {
            array[i][0] = statistics.get(i).getName();
            array[i][1] = numberFormat.format(statistics.get(i).getValue());
        }

        TableModel tableModel = new ArrayTableModel(array);
        TableBuilder tableBuilder = new TableBuilder(tableModel);
        tableBuilder.addFullBorder(BorderStyle.oldschool);
        shellHelper.print(tableBuilder.build().render(80));
    }

    public List<Statistic> getStatisticList(Long projectId, String analysisMethod) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Statistic> query = criteriaBuilder.createQuery(Statistic.class);

        Root<CommentAnalysisItem> root = query.from(CommentAnalysisItem.class);
        Join<CommentAnalysisItem, CommentAnalysis> commentsAnalysisJoin = root.join(CommentAnalysisItem_.commentAnalysis);
        Join<CommentAnalysis, Comment> commentsJoin = commentsAnalysisJoin.join(CommentAnalysis_.comment);
        Join<Comment, Issue> issueJoin = commentsJoin.join(Comment_.issue);
        Join<Issue, Project> projectJoin = issueJoin.join(Issue_.project);

        query.orderBy(criteriaBuilder.asc(root.get(CommentAnalysisItem_.NAME)));
        query.groupBy(root.get(CommentAnalysisItem_.name));
        query.where(
            criteriaBuilder.equal(projectJoin.get(Project_.id), projectId),
            criteriaBuilder.equal(commentsAnalysisJoin.get(CommentAnalysis_.code), analysisMethod)
        );

        Expression<Double> avg = criteriaBuilder.avg(root.get(CommentAnalysisItem_.VALUE));
        query.multiselect(root.get(CommentAnalysisItem_.NAME), avg);

        return entityManager.createQuery(query).getResultList();
    }

    public Long getCount(Long projectId, String analysisMethod) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);

        Root<CommentAnalysis> root = query.from(CommentAnalysis.class);
        Join<CommentAnalysis, Comment> commentsJoin = root.join(CommentAnalysis_.comment);
        Join<Comment, Issue> issueJoin = commentsJoin.join(Comment_.issue);
        Join<Issue, Project> projectJoin = issueJoin.join(Issue_.project);

        query.select(
            criteriaBuilder.count(
                root.get(CommentAnalysis_.id)
            )
        );
        query.where(
            criteriaBuilder.equal(projectJoin.get(Project_.id), projectId),
            criteriaBuilder.equal(root.get(CommentAnalysis_.code), analysisMethod)
        );

        return entityManager.createQuery(query).getSingleResult();
    }

}
