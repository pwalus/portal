package portal

import org.jsoup.Jsoup
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import portal.writer.Writer
import kotlin.math.ceil

@Component
class GitHubIssuesCrawler {

    private val logger = LoggerFactory.getLogger("github-logger")

    private val githubUrl = "https://github.com"

    fun fetch(repositoryId: String, issuesLimit: Int, writer: Writer) {
        val repositoryUrl = "$githubUrl/$repositoryId/"
        val issuesToVisit = getIssueToVisit(issuesLimit, repositoryUrl)

        writer.writeProject(repositoryId, repositoryUrl)
        issuesToVisit.forEach(saveIssue(writer, repositoryId, repositoryUrl))
    }

    private fun saveIssue(writer: Writer, repositoryId: String, repositoryUrl: String): (String) -> Unit {
        return { issueToVisit ->
            logger.info(issueToVisit)
            val issueId = issueToVisit.replace(repositoryUrl + "issues/", "")
            val dom = Jsoup.connect(issueToVisit).get();
            val title = dom.getElementsByClass("js-issue-title").first()

            writer.writeIssue(issueId, repositoryId, issueToVisit, title.text())

            val comments = dom.getElementsByClass("comment-body")
            val commentsText = comments.map { comment -> comment.select("p").eachText().joinToString(" ") }

            writer.writeComments(issueId, commentsText)
        }
    }

    private fun getIssueToVisit(issuesLimit: Int, repositoryUrl: String): List<String> {
        // "page" is a list of issues
        val pagesCountToVisit = ceil(issuesLimit.toDouble() / 25).toInt()
        val pagesToVisit = IntRange(1, pagesCountToVisit).map { page -> repositoryUrl + "issues?page=${page}" }

        return pagesToVisit.fold(emptyArray<String>(), { acc, pageToVisit ->
            arrayOf(*acc, *Jsoup.connect(pageToVisit).get()
                    .getElementsByAttributeValue("data-hovercard-type", "issue")
                    .eachAttr("href")
                    .map { suffix -> githubUrl + suffix }.toTypedArray()
            )
        }).slice(IntRange(0, issuesLimit - 1))
    }
}
