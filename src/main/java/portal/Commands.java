package portal;

import org.springframework.beans.factory.annotation.*;
import org.springframework.shell.standard.*;
import portal.writer.*;

@ShellComponent
class Commands {

    @Autowired
    private GitHubIssuesCrawler gitHubIssuesCrawler;

    @Autowired
    private Writer writer;

    @ShellMethod("Pobiera komentarze z podanego repozytorium GitHub i zapisuje do pliku csv")
    public void crawlGitHub(
        @ShellOption(help = "Nazwa repozytorium, z którego pobrać komentarze np. facebook/react lub vuejs/vue") String repositoryId,
        @ShellOption(help = "Z ilu issues pobrać komentarze", defaultValue = "10") String issuesLimit
    ) {
        gitHubIssuesCrawler.fetch(repositoryId, Integer.parseInt(issuesLimit), writer);
    }
}
