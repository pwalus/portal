package portal.command;

import org.springframework.beans.factory.annotation.*;
import org.springframework.shell.standard.*;
import portal.shell.*;
import portal.shell.chooser.*;
import portal.statistic.*;

@ShellComponent
public class StatisticCommand {

    private final ShellHelper shellHelper;

    private final InputReader inputReader;

    private final ProjectChooser projectChooser;

    private final AnalysisMethodChooser analysisMethodChooser;

    private final PrintStatistic printStatistics;

    private final PrintAllStatistics printAllStatistics;

    @Autowired
    public StatisticCommand(
        ShellHelper shellHelper,
        InputReader inputReader,
        ProjectChooser projectChooser,
        AnalysisMethodChooser analysisMethodChooser,
        PrintStatistic printStatistics,
        PrintAllStatistics printAllStatistics
    ) {
        this.shellHelper = shellHelper;
        this.inputReader = inputReader;
        this.projectChooser = projectChooser;
        this.analysisMethodChooser = analysisMethodChooser;
        this.printStatistics = printStatistics;
        this.printAllStatistics = printAllStatistics;
    }

    @ShellMethod(value = "Runs statistic by user input", key = "statistic:for:project")
    public void statisticForProject() {
        Long projectId = projectChooser.chooseProject();
        String method = analysisMethodChooser.chooseMethod();
        printStatistics.printFor(projectId, method);
    }

    @ShellMethod(value = "Print all statistics", key = "statistic:all")
    public void allStatistics() {
        printAllStatistics.print();
    }

}
