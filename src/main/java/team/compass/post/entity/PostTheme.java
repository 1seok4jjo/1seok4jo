package team.compass.post.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Entity
public class PostTheme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="post_theme")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "theme_id")
    private Theme theme;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
}