package team.compass.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.compass.post.dto.PhotoDto;
import team.compass.post.repository.PhotoRepository;
import team.compass.post.s3.AmazonS3ResourceStorage;
import team.compass.post.util.MultipartUtil;

@Service
@RequiredArgsConstructor
public class FileUploadService {
    private final AmazonS3ResourceStorage amazonS3ResourceStorage;

    private final PhotoRepository photoRepository;

    public PhotoDto save(MultipartFile multipartFile) {

        PhotoDto fileDetail = PhotoDto.multipartOf(multipartFile);
        String originalFilename = multipartFile.getOriginalFilename();
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
