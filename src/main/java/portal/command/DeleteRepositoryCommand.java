package portal.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import portal.domain.Project;
import portal.repository.ProjectRepository;

@ShellComponent
class DeleteRepositoryCommand {

    private final ProjectRepository projectRepository;

    @Autowired
    DeleteRepositoryCommand(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @ShellMethod("Usuwa wskazane repozytorium")
    public void deleteRepository(@ShellOption(help = "Nazwa repozytorium, które usunąć") String repositoryId)
        throws NoSuchFieldException {
        Project project = projectRepository.findByRepositoryId(repositoryId).orElseThrow(NoSuchFieldException::new);
        projectRepository.delete(project);
    }
}
