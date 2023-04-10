package team.compass.post.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.compass.post.dto.PhotoDto;
import team.compass.post.dto.PostDto;
import team.compass.post.entity.Photo;
import team.compass.post.entity.Post;
import team.compass.post.entity.PostPhoto;
import team.compass.post.entity.User;
import team.compass.post.repository.PhotoRepository;
import team.compass.post.repository.PostPhotoRepository;
import team.compass.post.repository.PostRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService{

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private FileUploadService fileUploadService;


    @Autowired
    private PhotoRepository photoRepository;
    @Autowired
    private PostPhotoRepository postPhotoRepository;

    // 메인 페이지 select
    @Override
    public ResponseEntity<Object> mainPageSelect() {
        //좋아요 + post name
        List<Post> list = postRepository.findList();
        for (Post post : list) {
            System.out.println("post = " + post);
        }
        //list 가져온걸 id 뽑아옴
        //1    1번글의1번사진 1번글의2번사진
        // 2  null
        // 3  3번글
        // 4 null
        List<Long> postIdList = list.stream().map(Post::getId).collect(Collectors.toList());
        // postIdList에 대한 사진 정보, 조건문 in
        List<Post> listById = postRepository.findListById(postIdList);
        // postId 중복 제거
        HashSet<Post> posts = new HashSet<>(listById);
        // 저장할 데이터 (결과)
        List<PostDto> result = new ArrayList<>();
        // set 데이터, list 데이터에 있는 id 비교해서 있으면 photo 붙임
        // 없다면 빼서 null 더하는 방식
        for (Post post : list) {
            //처음에 찾은 놈
            Post post1 = posts.stream().filter(i -> i.getId().equals(post.getId())).findAny().orElse(null);
            //1
            //2
            //3
            //4
            if (post1 != null) {
                result.add(new PostDto(post.getId(), post.getLikes().size(),
                        post1.getPhotos().stream().map(i -> i.getPhoto().getName()).collect(
                                Collectors.toList()), post.getTitle(), post.getLocation(), post.getStartDate(),post.getEndDate()));
            } else {
                result.add(new PostDto(
                        post.getId(),
                        post.getLikes().size(),
                        null,
                        post.getTitle(),
                        post.getLocation(),
                        post.getStartDate(),
                        post.getEndDate())
                );
            }
            //사진
        }
        return ResponseEntity.ok().body(result);
    }

    // 글 작성 (사진 포함 같이)
    @Override
    @Transactional
    public Post write(Post param, List<MultipartFile> multipartFile, User user){

        //post 저장
        //s3 -> 사진 외부 api
        //사진 저장
        // 원래는 사진 저장 -> 글 저장 (Post 저장) -> PostPhoto, Photo 저장 순으로 가야함.
        // 이유는 사진 저장은 외부 API(AWS)를 사용하는 거기에 속도가 느릴 수밖에 없음.
        // 지금은 1, 2번 순 바뀜.
        Post post = postRepository.save(param); // 글 저장  == Post 저장
        List<PostPhoto> list = getPhotos(multipartFile, user, post);
        postPhotoRepository.saveAll(list);

        return post;
    }

    private List<PostPhoto> getPhotos(List<MultipartFile> multipartFile, User user, Post post) {
        List<PostPhoto> list = new ArrayList<>(); // 사진 저장 리스트
        for (MultipartFile file : multipartFile) {
            PhotoDto photoDto = fileUploadService.save(file); // 하나씩 리스트로 저장
            Photo photo = photoRepository.save(photoDto.toEntity(user));
            list.add(new PostPhoto(post,photo)); // PostPhoto, Photo 저장
        }
        return list;
    }

    // 글 업데이트(기존 사진 삭제 -> 새 사진 업로드)
    @Override
    @Transactional
    public Post update(Post updatePost, List<MultipartFile> multipartFile, User user,Long postId){
        // post 업데이트

        Post udPost = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        udPost.setTitle(updatePost.getTitle()); // 제목
        udPost.setLocation(updatePost.getLocation()); // 장소
        udPost.setDetail(updatePost.getDetail()); // 내용
        // udPost.setCreatedAt(LocalDateTime.now()); // 수정일 -> 근데 등록일 기준으로 나중에 정렬하게 되면.. 이슈가 있을 수도?? 나중의 글 -> 새로 쓴 글로 바뀌어서..
        udPost.setStartDate(updatePost.getStartDate()); // 여행 시작
        udPost.setEndDate(updatePost.getEndDate()); // 여행 끝
        udPost.setHashtag(updatePost.getHashtag()); // 해시태그
        udPost.setUser(user);

        postRepository.save(udPost);

        //사진 저장
        List<PostPhoto> list = getPhotos(multipartFile, user, udPost);

        //이전의 사진데이터 삭제 -> 기준이 없기에
        List<PostPhoto> photos = udPost.getPhotos();

        postPhotoRepository.deleteAll(photos);

        //새로운 사진저장
        postPhotoRepository.saveAll(list);

        return udPost;
    }

    @Override
    @Transactional
    public void delete(Long postId) {
        // Post entity photos 부분 -> cascade = CascadeType.REMOVE
        // 함으로써 Post 삭제시 photos 도 삭제됨.
        // 처리 안 할 경우에 null 이 돼서 fk 가 보는 곳이 없어짐. 아니면 null 처리가 따로 있다던지..
        postRepository.deleteById(postId);

    }
}
