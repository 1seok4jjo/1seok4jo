package team.compass.photo.service;



import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.compass.common.config.AmazonS3ResourceStorage;
import team.compass.photo.repository.PhotoRepository;
import team.compass.photo.util.MultipartUtil;
import team.compass.post.dto.PhotoDto;

@Service
@RequiredArgsConstructor
public class FileUploadService {
    private final AmazonS3ResourceStorage amazonS3ResourceStorage;

    public PhotoDto save(MultipartFile multipartFile) {

        PhotoDto fileDetail = PhotoDto.multipartOf(multipartFile);
        String fileId = MultipartUtil.createFileId();
        String type = fileDetail.getType();

        String path = MultipartUtil.createPath(fileId, type);
        amazonS3ResourceStorage.store(path, multipartFile);
        fileDetail.setStoreFileUrl(path);
        //db에 저장하는로직 -> post ->
        // 사진 ->
        return fileDetail;
    }
}
