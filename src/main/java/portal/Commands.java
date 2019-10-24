package portal;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
class Commands {

    private GitHubIssuesCrawler gitHubIssuesCrawler;

    Commands(GitHubIssuesCrawler gitHubIssuesCrawler) {
        this.gitHubIssuesCrawler = gitHubIssuesCrawler;
    }

    @ShellMethod("Pobiera komentarze z podanego repozytorium GitHub i zapisuje do pliku csv")
    public void crawlGitHub(
            @ShellOption(help = "Nazwa repozytorium, z którego pobrać komentarze np. facebook/react lub vuejs/vue") String repositoryId,
            @ShellOption(help = "Ścieżka do pliku, gdzie zapisać komentarze") String resultFilePath,
            @ShellOption(help = "Z ilu issues pobrać komentarze", defaultValue = "10") String issuesLimit
    ) {
        gitHubIssuesCrawler.fetch(repositoryId, resultFilePath, Integer.parseInt(issuesLimit));
    }
}
