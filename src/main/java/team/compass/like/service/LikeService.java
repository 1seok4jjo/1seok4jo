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
}