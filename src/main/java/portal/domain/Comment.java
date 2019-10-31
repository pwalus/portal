package portal.domain;

import javax.persistence.*;
import javax.persistence.Entity;
import lombok.*;
import org.hibernate.annotations.*;

@Entity
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Type(type = "text")

    private String content;

    @ManyToOne
    @JoinColumn(name = "issue_id", nullable = false)
    private Issue issue;
}
