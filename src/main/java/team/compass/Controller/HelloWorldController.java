package team.compass.Controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {
    @GetMapping("/hello")
    public String test() {
        return "제로베이스 엄성준 2023 03 31 15:05분 테스트";
    }
}
