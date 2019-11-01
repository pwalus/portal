package portal.domain;

import java.util.*;
import javax.persistence.Entity;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.*;
import portal.domain.analysis.*;

@Entity
@Data
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NaturalId
    private String commentId;

    @Column(nullable = false)
    @Type(type = "text")
    private String content;

    @ManyToOne
    @JoinColumn(name = "issue_id", nullable = false)
    private Issue issue;

    @OneToMany(mappedBy = "comment")
    private List<CommentAnalysis> commentAnalysisList;

}
