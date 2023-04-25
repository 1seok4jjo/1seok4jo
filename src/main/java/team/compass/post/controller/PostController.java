package team.compass.post.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.compass.common.config.JwtTokenProvider;
import team.compass.common.utils.ResponseUtils;
import team.compass.post.controller.request.PostRequest;
import team.compass.post.controller.response.PostResponse;
import team.compass.post.domain.Post;
import team.compass.post.dto.PostDto;
import team.compass.post.service.PostService;
import team.compass.theme.domain.Theme;
import team.compass.user.domain.User;
import team.compass.user.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    private final UserRepository userRepository;

    private final JwtTokenProvider jwtTokenProvider;


    @ApiOperation(value = "해당 테마 페이지 글들 select 입니다.", notes = "테마 페이지 select")
    @GetMapping("/post")
    @Transactional(readOnly = true)
    public ResponseEntity<Object> getThemePage(
            @RequestParam(required = false) Integer lastId, // required default 값이 true... 첫 상단에는 null이어야 하니 false 로 잡음
            @RequestParam(defaultValue = "1") Integer themeId) {
        if (themeId > 10) {
            return ResponseUtils.notFound("테마 글 리스트를 찾을 수 없습니다.");
        }

        List<PostDto> posts = postService.themePageSelect(themeId, lastId);

        if (posts != null) {
            return ResponseUtils.ok("테마 글 리스트 조회에 성공하였습니다.", posts);
        } else {
            return ResponseUtils.notFound("테마 글 리스트를 찾을 수 없습니다.");
        }
    }

    @ApiOperation(value = "글 쓰기 api 입니다.", notes = "글작성, 사진첨부까지 함께 저장합니다.")
    @PostMapping(value = "/post")
    @Transactional
    public ResponseEntity<Object> postWrite(
            @RequestPart(value = "data") PostRequest postRequest, // request post 로 받아오기. 내용들
            @RequestPart(value = "images") List<MultipartFile> images,
            HttpServletRequest request) { // 이미지 받아오기
        User user = getUser(request);

        validationPhoto(images); // 유효성 검증(이미지 최대 5개까지 받아올 수 있게)

        Post postEntity = getPostEntity(postRequest); // toEntity -> 넣어주기 ( 📌이 부분 고칠것 - o)
        postEntity.setUser(user); // toEntity 에 user 값 넣기

        Post write = postService.write(postEntity, images, user); // 받았던 글 post(글과 사진, 유저) write 에 담기

        if (write != null) {
            return ResponseUtils.ok("글을 작성하였습니다.", "ok");
        } else {
            return ResponseUtils.notFound("글 작성에 실패하였습니다.");
        }
    }

    @ApiOperation(value = "글 수정 api 입니다.", notes = "글 수정, 사진 수정")
    @Transactional
    @PutMapping(value = "/post/{postId}")
    public ResponseEntity<Object> postUpdate(
            @RequestPart(value = "data") PostRequest postRequest,
            @RequestPart(value = "images") List<MultipartFile> images,
            @PathVariable Integer postId,
            HttpServletRequest request) { // 글 id 받기 위해

        User user = getUser(request);
        validationPhoto(images);
        Post updatePost = postService.update(getPostEntity(postRequest), images, user, postId); // 업데이트 받아온 것 저장

        if (updatePost != null) {
            return ResponseUtils.ok("글을 수정하였습니다.", "ok");//new PostResponse(updatePost)
        } else {
            return ResponseUtils.badRequest("글 수정에 실패하였습니다.");
        }
    }

    @ApiOperation(value = "글 삭제 api 입니다.", notes = "해당 글을 삭제하면 연관되어 있는 해당 글, 사진, 좋아요, 댓글 삭제됩니다.")
    @DeleteMapping(value = "/post/{postId}")
    @Transactional
    public ResponseEntity<Object> postDelete(@PathVariable Integer postId,HttpServletRequest request) {
        User user = getUser(request);
        boolean isDeleted = postService.delete(postId,user);
        if (isDeleted) {
            return ResponseUtils.ok("글을 삭제하였습니다.", null);
        } else {
            return ResponseUtils.badRequest("글 삭제에 실패하였습니다.");
        }
    }

    @ApiOperation(value = "글 상세 보기 입니다.", notes = "해당 글 상세 보기.")
    @Transactional(readOnly = true)
    @GetMapping(value = "/post/{postId}")
    public ResponseEntity<Object> getPost(@PathVariable Integer postId) {
        PostResponse post = postService.getPost(postId);

        if (post != null) {
            return ResponseUtils.ok("글 조회에 성공하였습니다.", post);
        } else {
            return ResponseUtils.notFound("해당 글을 찾을 수 없습니다.");
        }
    }


    private User getUser(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.resolveToken(request);
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        String userEmail =  authentication.getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));
        return user;
    }


    // 사진 5개 등록 제한걸어두기
    private static void validationPhoto(List<MultipartFile> images) {
        if (images.size() > 5) {
            throw new IllegalArgumentException("사진은 5장까지만 등록 가능합니다.");
        }
    }

    // toEntity 제거
    private static Post getPostEntity(PostRequest post) {
        return Post.builder()
                .title(post.getTitle())
                .detail(post.getDetail())
                .location(post.getLocation()) // 지역
                .hashtag(post.getHashtag()) // 해시태그
                .startDate(post.getStartDate())
                .endDate(post.getEndDate())
                .theme(new Theme(post.getThemeId()))
                .createdAt(LocalDateTime.now())
                .build();
    }
}

