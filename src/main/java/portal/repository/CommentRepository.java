package portal.repository;

import java.util.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;
import portal.domain.*;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    public Optional<Comment> findByCommentId(String commentId);
}
