package portal.writer;

import java.util.*;

public interface Writer {

    public void writeProject(String repositoryId, String repositoryUrl);

    public void writeIssue(String issueId, String repositoryId, String issueToVisit, String text);

    public void writeComments(String issueId, Map<String, String> commentsText);
}
