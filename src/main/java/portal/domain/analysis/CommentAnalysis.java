package portal.domain.analysis;

import javax.persistence.*;
import javax.persistence.Entity;
import lombok.*;
import org.hibernate.annotations.*;
import portal.domain.*;

@Entity
@Data
public class CommentAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Type(type = "text")
    private String json;

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

}
