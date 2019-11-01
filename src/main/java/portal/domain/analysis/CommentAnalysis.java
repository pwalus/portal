package portal.domain.analysis;

import java.util.*;
import javax.persistence.*;
import lombok.*;
import portal.domain.*;

@Entity
@Data
public class CommentAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String analysisCode;

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @OneToMany(mappedBy = "commentAnalysis")
    private List<CommentAnalysisItem> commentAnalysisItems;

}
