//package team.compass.like.service;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import team.compass.like.domain.Likes;
//import team.compass.like.dto.LikeDto;
//import team.compass.like.repository.LikeRepository;
//import team.compass.post.domain.Post;
//import team.compass.post.repository.PostRepository;
//import team.compass.user.domain.User;
//import team.compass.user.repository.UserRepository;
//
//@Service
//@RequiredArgsConstructor
//public class LikeService {
//
//    private final LikeRepository likeRepository;
//    private final PostRepository postRepository;
//    private final UserRepository userRepository;
//
//    // 좋아요 생성
//    public void add(LikeDto likeDto) throws Exception {
//
//        User user = userRepository.findById(likeDto.getUserId())
//                .orElseThrow(() -> new RuntimeException("로그인 후에 사용해주세요."));
//
//        Post post = postRepository.findById(likeDto.getPostId())
//                .orElseThrow(() -> new RuntimeException("게시글이 없습니다."));
//
//        if (likeRepository.findByPostAndUser(post, user).isPresent()) {
//            throw new Exception();
//        }
//
//        if (postRepository.findByIdAndUserId(likeDto.getPostId(),
//                likeDto.getUserId()).isPresent()) {
//            throw new Exception("본인 글에는 좋아요가 불가합니다.");
//        }
//
//        Likes likes = Likes.builder()
//                .post(post)
//                .user(user)
//                .build();
//
//        likeRepository.save(likes);
//    }
//
//    // 좋아요 취소
//    public void cancel(LikeDto likeDto) throws Exception {
//        User user = userRepository.findById(likeDto.getUserId())
//                .orElseThrow(() -> new RuntimeException("로그인 후에 사용해주세요."));
//
//        Post post = postRepository.findById(likeDto.getPostId())
//                .orElseThrow(() -> new RuntimeException("게시글이 없습니다."));
//
//        Likes likes = likeRepository.findByPostAndUser(post, user)
//                .orElseThrow(() -> new RuntimeException("이미 좋아요 한 상태입니다."));
//
//        likeRepository.delete(likes);
//    }
//
//}