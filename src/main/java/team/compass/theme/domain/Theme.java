package team.compass.theme.domain;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import team.compass.post.domain.Post;

@Entity
@NoArgsConstructor
@Getter
public class Theme {
    @Id
    @Column(name = "theme_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    @OneToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public Theme(Integer id) {
        this.id = id;
    }
}
