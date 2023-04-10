package team.compass.post.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import team.compass.post.entity.Photo;
import team.compass.post.entity.User;
import team.compass.post.util.MultipartUtil;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class PhotoDto {
    private String uuid;
    private String name;
    private String type;
    private String storeFileUrl;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    public static PhotoDto multipartOf(MultipartFile multipartFile) {
        final String fileId = MultipartUtil.createFileId();
        final String type = MultipartUtil.getType(multipartFile.getContentType());
        return PhotoDto.builder()
                .uuid(fileId)
                .name(multipartFile.getOriginalFilename())
                .type(type)

                .build();
    }

    public Photo toEntity(User user) {
        return Photo.builder()
                .type(this.type)
                .name(this.name)
                .user(user)
                .storeFileUrl(this.storeFileUrl)
                .createdAt(LocalDateTime.now())
                .build();
    }
}

