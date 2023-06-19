package team.compass.post.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.compass.post.controller.response.PostResponse;
import team.compass.post.domain.Post;
import team.compass.post.dto.PostDto;
import team.compass.user.domain.User;

@Service
public interface PostService {


    Post write(Post param, List<MultipartFile> multipartFile, User user);

    Post update(Post param, List<MultipartFile> multipartFile, User user, Integer postId);


    boolean delete(Integer postId, User user);

    PostResponse getPost(Integer postId);


    List<PostDto> themePageSelect(Integer themeId, Integer lastId);
}
