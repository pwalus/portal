package portal;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class GitHubIssuesCrawler {

    void fetch(String repositoryId, String resultFilePath, Integer issuesLimit) throws IOException {
         Path path = FileSystems.getDefault().getPath(resultFilePath);
         Files.createDirectory(path);

        // "page" is a list of issues
        int pagesCountToVisit = (int) Math.ceil(issuesLimit.doubleValue() / 25);
        List<String> pagesToVisit = IntStream.rangeClosed(1, pagesCountToVisit).collect(
                ArrayList::new,
                (result, page) -> result.add("https://github.com/" + repositoryId + "/issues?page=" + page),
                ArrayList::addAll
        );

        List<String> issuesToVisit = pagesToVisit.stream().collect(
                ArrayList<String>::new,
                (result, pageToVisit) -> {
                    try {
                        result.addAll(
                                Jsoup.connect(pageToVisit).get()
                                        .getElementsByAttributeValue("data-hovercard-type", "issue")
                                        .eachAttr("href")
                                        .stream()
                                        .map(suffix -> "https://github.com" + suffix)
                                        .collect(Collectors.toList())
                        );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                },
                ArrayList::addAll
        ).subList(0, issuesLimit);

        List<String> comments = issuesToVisit.stream().collect(
                ArrayList::new,
                (result, issueToVisit) -> {
                    try {
                        result.addAll(Jsoup.connect(issueToVisit).get().getElementsByClass("comment-body").eachText());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                },
                ArrayList::addAll
        );

    }
}
