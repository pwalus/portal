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
    private String issueId;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @OneToMany(mappedBy = "issue")
    private List<Comment> comments;
}
