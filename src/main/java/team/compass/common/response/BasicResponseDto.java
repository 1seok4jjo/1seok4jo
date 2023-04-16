package team.compass.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BasicResponseDto {
    private Integer statusCode;
    private String message;
//    private T data;
}
