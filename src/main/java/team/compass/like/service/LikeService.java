package team.compass.like.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.compass.like.domain.Likes;
import team.compass.like.dto.LikeDto;
import team.compass.like.repository.LikeRepository;
import team.compass.post.domain.Post;
import team.compass.post.repository.PostRepository;
import team.compass.user.domain.User;
import team.compass.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public boolean saveLike(LikeDto likeDto) {

        boolean state = true;

        User user = userRepository.findById(likeDto.getUserId()).orElse(null);
        
        Post post =
                postRepository.findById(likeDto.getPostId()).orElse(null);

        Likes checkLike =
                likeRepository.findAllByPost_IdAndUser_Id(likeDto.getPostId()
                        , likeDto.getUserId()).orElse(null);

        if (checkLike != null) {
            likeRepository.delete(checkLike);
            state = false;

        } else {
            likeRepository.save(Likes.builder()
                    .post(post)
                    .user(user)
                    .build());
        }

        return state;
    }



    public void addLike(Integer userId, Integer postId) {
        postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("글 없음"));
        Optional<Likes> likes = likeRepository
            .findByUserIdAndPostId(userId, postId);
        if (likes.isPresent()) {
            throw new IllegalArgumentException("이미 좋아요 등록");
        }
        Likes entityLikes = likes.get();
        likeRepository.save(entityLikes);
    }

    public void cancelLikes(User user, Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("글 없음"));

        Likes likes = likeRepository.findByUserIdAndPostId(user.getId(), postId)
                .orElseThrow(() -> new IllegalArgumentException("사용자나 글 조회가 안 됨. 또는 사용자 권한이 없음."));
        likeRepository.delete(likes);
    }


}