package portal.shell.chooser;

import java.util.*;
import java.util.stream.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import portal.domain.*;
import portal.repository.*;
import portal.shell.*;

@Component
public class ProjectChooser {

    private final ProjectRepository projectRepository;

    private final InputReader inputReader;

    @Autowired
    public ProjectChooser(ProjectRepository projectRepository, InputReader inputReader) {
        this.projectRepository = projectRepository;
        this.inputReader = inputReader;
    }

    public Long chooseProject() {
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

        return Long.valueOf(projectValue);
    }
}
