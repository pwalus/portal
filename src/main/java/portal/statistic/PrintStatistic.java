package portal.statistic;

import java.util.*;
import javax.persistence.*;
import javax.persistence.criteria.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.shell.table.*;
import org.springframework.stereotype.*;
import portal.domain.*;
import portal.domain.analysis.*;
import portal.shell.*;

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

        for (int i = 0; i < statistics.size(); i++) {
            array[i][0] = statistics.get(i).getName();
            array[i][1] = String.valueOf(statistics.get(i).getValue());
        }

        TableModel tableModel = new ArrayTableModel(array);
        TableBuilder tableBuilder = new TableBuilder(tableModel);

        tableBuilder.addFullBorder(BorderStyle.oldschool);
        shellHelper.print(tableBuilder.build().render(80));
    }

    private List<Statistic> getStatisticList(Long projectId, String analysisMethod) {
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

        return entityManager.createQuery(query).getResultList();
    }

}
