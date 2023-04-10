package team.compass.post.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.compass.post.controller.request.PostRequest;
import team.compass.post.controller.response.PostResponse;
import team.compass.post.entity.Post;
import team.compass.post.entity.User;
import team.compass.post.repository.PostRepository;
import team.compass.post.repository.UserRepository;
import team.compass.post.service.PostService;

import java.util.List;

@RestController
public class PostController {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;
    @Autowired
    private UserRepository userRepository;

    @ApiOperation(value = "메인 페이지 select 입니다.", notes = "메인 페이지 select")
    @GetMapping("/main")
    public ResponseEntity<Object> getMain() {
        ResponseEntity<Object> pageSelect = postService.mainPageSelect();
        return pageSelect;
    }

    @ApiOperation(value = "글 쓰기 api 입니다.", notes = "글작성, 사진첨부까지 함께 저장합니다.")
    @PostMapping(value = "/post/write")
    public ResponseEntity<Object> postWrite(
            @RequestPart(value = "data") PostRequest post,
            @RequestPart(value = "images") List<MultipartFile> images) {
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다.")); // user 체크 / 실제로 로그인이 들어가야 합니다
        Post postEntity = post.toEntity();
        postEntity.setUser(user); // 로그인
        Post write = postService.write(postEntity, images, user);
        return ResponseEntity.ok(write);
    }

    @ApiOperation(value = "글 수정 api 입니다.", notes = "글 수정, 사진 수정")
    @PutMapping(value = "/post/update/{postId}")
    public ResponseEntity<Object> postUpdate(
            @RequestPart(value = "data") PostRequest post,
            @RequestPart(value = "images") List<MultipartFile> images,
            @PathVariable Long postId) {

        User user = userRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다.")); // user 체크

        Post updatePost = postService.update(post.toEntity(), images, user, postId);

        return ResponseEntity.ok(new PostResponse(updatePost));
    }

    @ApiOperation(value = "글 삭제 api 입니다.", notes = "해당 글을 삭제하면 연관되어 있는 해당 글, 사진, 좋아요, 댓글 삭제됩니다.")
    @DeleteMapping(value = "/post/delete/{postId}")
    public ResponseEntity<Object> postDelete(@PathVariable Long postId) {
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다.")); // user 체크
        postService.delete(postId);
        return ResponseEntity.ok("삭제되었습니다.");
    }
}
