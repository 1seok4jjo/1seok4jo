package team.compass.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class KakaoUserDto {
    Long userId;
    String token;
    String email;
}
