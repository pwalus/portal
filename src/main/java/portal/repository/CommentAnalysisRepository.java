package portal.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;
import portal.domain.analysis.*;

@Repository
public interface CommentAnalysisRepository extends JpaRepository<CommentAnalysis, Long> {

}
