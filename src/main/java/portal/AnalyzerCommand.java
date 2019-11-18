package portal;

import org.springframework.beans.factory.annotation.*;
import org.springframework.shell.standard.*;
import portal.analyser.*;
import portal.thread.*;
import portal.writer.*;

@ShellComponent
class AnalyzerCommand {

    private GitHubIssuesCrawler gitHubIssuesCrawler;

    private Writer writer;

    private CommentAnalyser commentAnalyser;

    private ThreadManager threadManager;

    @Autowired
    AnalyzerCommand(
        GitHubIssuesCrawler gitHubIssuesCrawler,
        Writer writer,
        CommentAnalyser commentAnalyser,
        ThreadManager threadManager
    ) {
        this.gitHubIssuesCrawler = gitHubIssuesCrawler;
        this.writer = writer;
        this.commentAnalyser = commentAnalyser;
        this.threadManager = threadManager;
    }

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
