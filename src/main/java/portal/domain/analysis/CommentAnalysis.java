package portal.domain.analysis;

import java.util.*;
import javax.persistence.*;
import lombok.*;
import portal.domain.*;

@Entity
@Data
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"code", "comment_id"}))
public class CommentAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code;

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @OneToMany(mappedBy = "commentAnalysis")
    private List<CommentAnalysisItem> commentAnalysisItems;

    @Override
    public String toString() {
        return "CommentAnalysis{" +
            "id=" + id +
            ", code='" + code + '\'' +
            ", comment=" + comment +
            '}';
    }
}
