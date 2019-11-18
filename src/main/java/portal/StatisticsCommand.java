package portal;

import java.util.*;
import java.util.stream.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.shell.standard.*;
import portal.domain.*;
import portal.repository.*;
import portal.shell.*;

@ShellComponent
public class StatisticsCommand {

    private final ShellHelper shellHelper;


    private final InputReader inputReader;

    private final ProjectRepository projectRepository;

    @Autowired
    public StatisticsCommand(
        ShellHelper shellHelper,
        InputReader inputReader,
        ProjectRepository projectRepository
    ) {
        this.shellHelper = shellHelper;
        this.inputReader = inputReader;
        this.projectRepository = projectRepository;
    }

    @ShellMethod("Runs statistics by user input")
    public void run() {
        Map<String, String> options = projectRepository.findAll()
            .stream()
            .collect(Collectors
                .toMap(project -> String.valueOf(project.getId()), Project::getRepositoryId)
            );

        String projectValue = inputReader
            .selectFromList(
                "Choose project",
                "Please enter one of the [] values",
                options,
                true,
                null
            );

        String project = String.valueOf(options.get(projectValue.toUpperCase()));

        shellHelper.printInfo(project);

    }

}
