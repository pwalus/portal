package portal

import com.opencsv.CSVWriter
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.OutputStreamWriter
import java.nio.file.FileSystems
import java.nio.file.Files
import kotlin.math.ceil

class GitHubIssuesCrawler {

    fun fetch(repositoryId: String, resultFilePath: String, issuesLimit: Int) {
        val path = FileSystems.getDefault().getPath(resultFilePath)
        val os = Files.newOutputStream(path)
        val osw = OutputStreamWriter(os)
        val csvw = CSVWriter(osw);

        // "page" is a list of issues
        val pagesCountToVisit = ceil(issuesLimit.toDouble() / 25).toInt()
        val pagesToVisit = IntRange(1, pagesCountToVisit).map { page -> "https://github.com/${repositoryId}/issues?page=${page}" }

        val issuesToVisit = pagesToVisit.fold(emptyArray<String>(), { acc, pageToVisit ->
            arrayOf(*acc, *Jsoup.connect(pageToVisit).get()
                    .getElementsByAttributeValue("data-hovercard-type", "issue")
                    .eachAttr("href")
                    .map { suffix -> "https://github.com${suffix}" }.toTypedArray()
            )
        }).slice(IntRange(0, issuesLimit - 1))

        csvw.writeNext(arrayOf(repositoryId))
        issuesToVisit.forEach { issueToVisit ->
            val comments = Jsoup.connect(issueToVisit).get().getElementsByClass("comment-body")
            val commentsText = comments.map { comment -> comment.select("p").eachText().joinToString(" ") }
            csvw.writeAll(commentsText.map { comment -> arrayOf(comment) })
        }

        csvw.close()
        osw.close()
        os.close()
    }
}
