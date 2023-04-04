package team.compass.user.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import team.compass.user.domain.SignUpInput;
import team.compass.user.domain.model.User;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SignUpUserServiceTest {
    @Autowired
    private SignUpUserService service;

    @Test
    void signUp() {
        SignUpInput parameter = SignUpInput.builder()
                .email("compass@gmail.com")
                .password("1234")
                .build();

        User signUpResult = service.signUp(parameter);

        Assert.isTrue(signUpResult != null);
    }
}