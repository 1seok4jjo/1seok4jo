package team.compass.post.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.compass.post.domain.Post;
import team.compass.post.dto.PostDto;
import team.compass.user.domain.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public interface PostService {

    // 메인 페이지 select
    Post write(Post param, List<MultipartFile> multipartFile, User user);

    Post update(Post param, List<MultipartFile> multipartFile, User user, Integer postId);


//    void delete(Integer postId);
    boolean delete(Integer postId);

    Post getPost(Integer postId);

    Post getUserByLikePost(HttpServletRequest request);

    List<PostDto> themePageSelect(Integer themeId, Integer lastId);
}
