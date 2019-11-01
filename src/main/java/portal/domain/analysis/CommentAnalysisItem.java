package portal.domain.analysis;

import javax.persistence.*;
import lombok.*;

@Entity
@Data
public class CommentAnalysisItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String value;

    @ManyToOne
    @JoinColumn(name = "comment_analysis_id", nullable = false)
    private CommentAnalysis commentAnalysis;

}
