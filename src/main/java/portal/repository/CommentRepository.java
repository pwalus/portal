package portal.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;
import portal.domain.*;

@Repository
interface CommentRepository extends JpaRepository<Comment, Long> {

}
