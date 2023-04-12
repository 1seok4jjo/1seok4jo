package team.compass.post.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import io.swagger.annotations.ApiOperation;
import team.compass.member.domain.User;
import team.compass.member.repository.UserRepository;
import team.compass.post.controller.request.PostRequest;
import team.compass.post.controller.response.PostResponse;
import team.compass.post.domain.Post;
import team.compass.post.service.PostService;

@RestController
public class PostController {


    @Autowired
    private PostService postService;
    @Autowired
    private UserRepository userRepository;

    @ApiOperation(value = "메인 페이지 select 입니다.", notes = "메인 페이지 select")
    @GetMapping("/main")
    @Transactional(readOnly = true)
    public ResponseEntity<Object> getMain() {
        return postService.mainPageSelect();
    }

    @ApiOperation(value = "글 쓰기 api 입니다.", notes = "글작성, 사진첨부까지 함께 저장합니다.")
    @PostMapping(value = "/post")
    @Transactional
    public ResponseEntity<Object> postWrite(
            @RequestPart(value = "data") PostRequest post,
            @RequestPart(value = "images") List<MultipartFile> images) {

        validationPhoto(images);
        User user = userRepository.findById(1)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));

        Post postEntity = post.toEntity();
        System.out.println("postEntity.getTheme().getId() = " + postEntity.getTheme().getId());
        postEntity.setUser(user);

        Post write = postService.write(postEntity, images, user);

        return ResponseEntity.ok("ok");
    }

    private static void validationPhoto(List<MultipartFile> images) {
        if (images.size() > 5) {
            throw new IllegalArgumentException("사진은 5장까지만 등록 가능합니다.");
        }
    }

    @ApiOperation(value = "글 수정 api 입니다.", notes = "글 수정, 사진 수정")
    @Transactional
    @PutMapping(value = "/post/{postId}")
    public ResponseEntity<Object> postUpdate(
            @RequestPart(value = "data") PostRequest post,
            @RequestPart(value = "images") List<MultipartFile> images,
            @PathVariable Integer postId) {

        validationPhoto(images);
        User user = userRepository.findById(1)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다.")); // user 체크

        Post updatePost = postService.update(post.toEntity(), images, user, postId);

        return ResponseEntity.ok(new PostResponse(updatePost));
    }

    @ApiOperation(value = "글 삭제 api 입니다.", notes = "해당 글을 삭제하면 연관되어 있는 해당 글, 사진, 좋아요, 댓글 삭제됩니다.")
    @DeleteMapping(value = "/post/{postId}")
    @Transactional
    public ResponseEntity<Object> postDelete(@PathVariable Integer postId) {
        User user = userRepository.findById(1)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다.")); // user 체크
        postService.delete(postId);
        return ResponseEntity.ok("삭제되었습니다.");
    }

    @ApiOperation(value = "글 상세 보기 입니다.", notes = "해당 글 상세 보기.")
    @Transactional(readOnly = true)
    @GetMapping(value = "/post/{postId}")
    public ResponseEntity<Object> getPost(@PathVariable Integer postId) {
        Post post = postService.getPost(postId);
        return ResponseEntity.ok(new PostResponse(post));
    }

}

