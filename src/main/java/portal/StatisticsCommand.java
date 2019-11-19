package portal;

import org.springframework.beans.factory.annotation.*;
import org.springframework.shell.standard.*;
import portal.shell.*;
import portal.shell.chooser.*;
import portal.statistic.*;

@ShellComponent
public class StatisticsCommand {

    private final ShellHelper shellHelper;

    private final InputReader inputReader;

    private final ProjectChooser projectChooser;

    private final AnalysisMethodChooser analysisMethodChooser;

    private final PrintStatistic printStatistics;

    @Autowired
    public StatisticsCommand(
        ShellHelper shellHelper,
        InputReader inputReader,
        ProjectChooser projectChooser,
        AnalysisMethodChooser analysisMethodChooser,
        PrintStatistic printStatistics
    ) {
        this.shellHelper = shellHelper;
        this.inputReader = inputReader;
        this.projectChooser = projectChooser;
        this.analysisMethodChooser = analysisMethodChooser;
        this.printStatistics = printStatistics;
    }

    @ShellMethod("Runs statistics by user input")
    public void run() {
        Long projectId = projectChooser.chooseProject();
        String method = analysisMethodChooser.chooseMethod();
        printStatistics.printFor(projectId, method);
    }

}
