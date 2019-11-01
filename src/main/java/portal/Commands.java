package portal;

import org.springframework.beans.factory.annotation.*;
import org.springframework.shell.standard.*;
import portal.analyser.*;
import portal.thread.*;
import portal.writer.*;

@ShellComponent
class Commands {

    @Autowired
    private GitHubIssuesCrawler gitHubIssuesCrawler;

    @Autowired
    private Writer writer;

    @Autowired
    private CommentAnalyser commentAnalyser;

    @Autowired
    private ThreadManager threadManager;

    @ShellMethod("Pobiera komentarze z podanego repozytorium GitHub i zapisuje do bazy danych. Podczas przetwarzania inny wątek pobiera nowo powstałe wpisy i je analizuje")
    public void crawlGitHub(
        @ShellOption(help = "Nazwa repozytorium, z którego pobrać komentarze np. facebook/react lub vuejs/vue") String repositoryId,
        @ShellOption(help = "Z ilu issues pobrać komentarze", defaultValue = "10") String issuesLimit
    ) {
        threadManager.addTask(() -> gitHubIssuesCrawler.fetch(repositoryId, Integer.parseInt(issuesLimit), writer));
        threadManager.addTask(() -> commentAnalyser.analyse());
        threadManager.run();
    }
}
