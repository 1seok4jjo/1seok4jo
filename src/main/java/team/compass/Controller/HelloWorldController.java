package team.compass.Controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {
    @GetMapping("/hello")
    public String test() {
        return "제로베이스 김정렬 2023 03 31 15:11분 테스트";
    }
}
