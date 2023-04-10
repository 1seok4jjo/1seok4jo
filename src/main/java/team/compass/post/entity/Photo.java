package team.compass.post.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="photo_id")
    private Long id;

    private String type;

    private String name;

    private String storeFileUrl;

    private LocalDateTime createdAt;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "photo")
    private List<PostPhoto> postPhotos;

    public Photo(String name, String storeFileUrl) {
        this.name = name;
        this.storeFileUrl = storeFileUrl;
    }

    public Photo toEntity() {
        Photo photo = Photo.builder()
                .id(id)
                .type(type)
                .name(name)
                .storeFileUrl(storeFileUrl)
                .createdAt(createdAt)
                .build();
        return photo;
    }
}
