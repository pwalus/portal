package portal.repository;

import java.util.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;
import portal.domain.*;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    public Optional<Project> findByRepositoryId(String repositoryId);

}
