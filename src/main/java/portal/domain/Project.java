package portal.domain;

import java.util.*;
import javax.persistence.*;
import javax.persistence.Entity;
import lombok.*;
import org.hibernate.annotations.*;

@Entity
@Data
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NaturalId
    private String repositoryId;

    @Column(nullable = false)
    private String url;

    @OneToMany(mappedBy = "repository")
    private List<Issue> issues;
}
