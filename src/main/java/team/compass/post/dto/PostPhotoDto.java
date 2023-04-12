package team.compass.post.dto;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team.compass.photo.domain.Photo;
import team.compass.post.domain.Post;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class PostPhotoDto {
    private Long id;
    private Post post;

    private Photo photo;

    public PostPhotoDto toEntity() {
        return PostPhotoDto.builder()
            .post(this.post)
            .photo(this.photo)
            .build();
    }
}
