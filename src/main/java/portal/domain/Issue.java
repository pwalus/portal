package portal.domain;

import java.util.*;
import javax.persistence.Entity;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.*;

@Entity
@Data
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NaturalId
    private Long issueId;

    @Column(nullable = false)
    private Long title;

    @ManyToOne
    @JoinColumn(name = "repository_id", nullable = false)
    private Project repository;

    @OneToMany(mappedBy = "issue")
    private List<Comment> comments;
}
