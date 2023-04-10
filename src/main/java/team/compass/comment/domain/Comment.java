package team.compass.comment.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import team.compass.member.domain.User;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long commentId;
    @ManyToOne //comment(M) User(O)
    @JoinColumn
    private User writer;
    @ManyToOne //comment(M) Post(O)
    @JoinColumn
    private Post post;
    @Column
    private String content;
    @Column
    private LocalDateTime createdTime;


}
