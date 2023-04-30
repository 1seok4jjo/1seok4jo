package team.compass.like.service;

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



    public boolean addLike(User user, Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("글 없음"));

        if (isNotAlreadyLikes(user, post)) {
            likeRepository.save(new Likes(post, user));
            return true;
        }
        return false;
    }

    public void cancelLikes(User user, Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("글 없음"));

        Likes likes = likeRepository.findByUserAndPost(user, post)
                .orElseThrow(() -> new IllegalArgumentException("사용자나 글 조회가 안 됨. 또는 사용자 권한이 없음."));

        likeRepository.delete(likes);
    }

    private boolean isNotAlreadyLikes(User user, Post post) {
        return likeRepository.findByUserAndPost(user, post).isEmpty();
    }
}