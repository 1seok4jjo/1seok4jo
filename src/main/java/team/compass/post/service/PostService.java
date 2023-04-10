package team.compass.post.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.compass.post.entity.Post;
import team.compass.post.entity.User;

import java.util.List;

@Service
public interface PostService {
    // 메인 페이지 select
    ResponseEntity<Object> mainPageSelect();


    @Transactional
    Post write(Post param, List<MultipartFile> multipartFile, User user);


    @Transactional
    Post update(Post param, List<MultipartFile> multipartFile, User user, Long postId);

    @Transactional
    void delete(Long postId);
}
