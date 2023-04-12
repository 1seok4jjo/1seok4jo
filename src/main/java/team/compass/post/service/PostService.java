package team.compass.post.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.compass.member.domain.User;
import team.compass.post.domain.Post;

import java.util.List;

@Service
public interface PostService {
    // 메인 페이지 select
    ResponseEntity<Object> mainPageSelect();

    Post write(Post param, List<MultipartFile> multipartFile, User user);

    Post update(Post param, List<MultipartFile> multipartFile, User user, Integer postId);


    void delete(Integer postId);

    Post getPost(Integer postId);
}
