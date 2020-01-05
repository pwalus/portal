package portal.statistic;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.shell.table.ArrayTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.TableBuilder;
import org.springframework.shell.table.TableModel;
import org.springframework.stereotype.Component;
import portal.analyser.api.configuration.ApiConfiguration;
import portal.domain.Project;
import portal.repository.ProjectRepository;
import portal.shell.ShellHelper;

@Component
public class PrintAllStatistics {

    private final PrintStatistic printStatistic;

    private final ProjectRepository projectRepository;

    private final ApiConfiguration apiConfiguration;

    private final ShellHelper shellHelper;

    public PrintAllStatistics(
        PrintStatistic printStatistic,
        ProjectRepository projectRepository,
        ApiConfiguration apiConfiguration,
        ShellHelper shellHelper
    ) {
        this.printStatistic = printStatistic;
        this.projectRepository = projectRepository;
        this.apiConfiguration = apiConfiguration;
        this.shellHelper = shellHelper;
    }

    public void print() {
        List<Project> projects = projectRepository.findAll();
        List<String> codes = apiConfiguration.getAnalysisCodes();

        for (String code : codes) {
            shellHelper.printSuccess(code);

            Map<String, List<Statistic>> allStatistics = new HashMap<>();
            int maxRows = 0;
            for (Project project : projects) {
                Long count = printStatistic.getCount(project.getId(), code);
                shellHelper.printWarning(project.getRepositoryId() + " : " + count.toString());

                List<Statistic> statistics = printStatistic.getStatisticList(project.getId(), code);
                maxRows = Integer.max(maxRows, statistics.size());
                allStatistics.put(
                    project.getRepositoryId(),
                    statistics
                );
            }

            String[][] array = new String[maxRows + 1][projects.size() + 1];
            array[0][0] = "Code";
            for (int i = 1; i <= projects.size(); i++) {
                array[0][i] = projects.get(i - 1).getRepositoryId();
            }

            int i = 1;
            for (Map.Entry<String, List<Statistic>> entry : allStatistics.entrySet()) {
                for (Statistic statistic : entry.getValue()) {
                    array[i++][0] = statistic.getName();
                }
                break;
            }

            int y = 1;
            for (Project project : projects) {
                int x = 1;
                List<Statistic> statistics = allStatistics.get(project.getRepositoryId());
                if (statistics == null) {
                    continue;
                }
                NumberFormat numberFormat = NumberFormat.getPercentInstance();
                for (Statistic statistic : statistics) {
                    array[x][y] = numberFormat.format(statistic.getValue());
                    x++;
                }
                y++;
            }

            TableModel tableModel = new ArrayTableModel(array);
            TableBuilder tableBuilder = new TableBuilder(tableModel);
            tableBuilder.addFullBorder(BorderStyle.oldschool);
            shellHelper.print(tableBuilder.build().render(40));
        }
    }
}
