package portal.shell.chooser;

import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import portal.domain.Project;
import portal.repository.ProjectRepository;
import portal.shell.InputReader;

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

    public Project chooseProject(Long projectId) {
        return projectRepository.getOne(projectId);
    }
}
