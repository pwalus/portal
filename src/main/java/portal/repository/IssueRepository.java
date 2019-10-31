package portal.repository;

import java.util.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;
import portal.domain.*;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {

    public Optional<Issue> findByIssueId(String issueId);

}
