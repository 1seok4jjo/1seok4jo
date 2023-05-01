package team.compass.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.mock.web.MockPart;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import springfox.documentation.spring.web.json.Json;
import team.compass.common.config.JwtTokenProvider;
import team.compass.common.utils.MailUtils;
import team.compass.user.domain.User;
import team.compass.user.dto.*;
import team.compass.user.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceImplTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();

    private MockMultipartHttpServletRequest multipartHttpServletRequest = new MockMultipartHttpServletRequest();



    @BeforeEach
    void signup() {
        UserRequest.SignUp user = UserRequest.SignUp.builder()
                .email("test100@test.com")
                .password("1234")
                .build();


        userService.signUp(user, multipartHttpServletRequest);
    }

    @Test
    void signUpSuccess() {
        // given
        UserRequest.SignUp signUpDto = UserRequest.SignUp.builder()
                .email("test33@test.com")
                .password("1234")
                .build();


        // when
        User signupByUser = userService.signUp(signUpDto, multipartHttpServletRequest);
        // then
        Assert.assertNotNull(signupByUser);
    }

    @Test
    void signUpByExistEmail() {
        // given
        UserRequest.SignUp userRequest = UserRequest.SignUp.builder()
                .email("test100@test.com")
                .password("111")
                .build();

        // when
//        User user = userService.signUp(userRequest, request);

        // then
        assertThrows(RuntimeException.class, () -> userService.signUp(userRequest, multipartHttpServletRequest));
    }

    @Test
    void signInSuccess() {
        // given
        UserRequest.SignIn request = UserRequest.SignIn.builder()
                .email("test100@test.com")
                .password("1234")
                .build();

        // when
        UserResponse response = userService.signIn(request);

        // then
        Assert.assertNotNull(response);
    }

    @Test
    void signInNotEqualPassword() {
        // given
        UserRequest.SignIn request = UserRequest.SignIn.builder()
                .email("test100@test.com")
                .password("124")
                .build();

        // when

        // then
        assertThrows(RuntimeException.class, () -> userService.signIn(request));
    }

    @Test
    void updateUser() {
        // given
        UserRequest.SignIn signInRequest = UserRequest.SignIn.builder()
                .email("test100@test.com")
                .password("1234")
                .build();
        UserResponse user = userService.signIn(signInRequest);

        String token = user.getAccessToken();

        UserUpdate updateUserRequest = UserUpdate.builder()
                .password("1234")
                .introduction("test333")
                .build();


        httpServletRequest.addHeader("Authorization", "bearer " + token);

        // when
        User updateUser = userService.updateUser(updateUserRequest, httpServletRequest, multipartHttpServletRequest);

        // then
        Assert.assertNotNull(updateUser);
    }

    @Test
    void updateUserNotEqualPassword() {
        UserRequest.SignIn signInRequest = UserRequest.SignIn.builder()
                .email("test100@test.com")
                .password("1234")
                .build();
        UserResponse user = userService.signIn(signInRequest);

        String token = user.getAccessToken();
        UserUpdate updateUserRequest = UserUpdate.builder()
                .password("1233")
                .introduction("test333")
                .build();

        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();

        httpServletRequest.addHeader("Authorization", "bearer " + token);


        // when then
        assertThrows(RuntimeException.class, () -> userService.updateUser(updateUserRequest, httpServletRequest, multipartHttpServletRequest));
    }

    @Test
    void logout() {
        // given
        String token = getTestUserByToken();
        httpServletRequest.addHeader("Authorization", "bearer " + token);
        // when
        userService.logout(httpServletRequest);
        // then
        assertEquals(-1, jwtTokenProvider.validateToken(token));
    }

    @Test
    void resetPassword() {
        // given
        UserResponse response = testBySignIn();
        User user = userRepository.findByEmail(response.getEmail())
                .orElse(null);

        user.setResetPasswordKey("12345");
        PasswordResetRequest request = new PasswordResetRequest(user.getEmail(), "123", "12345");
        // when
        userService.resetPassword(request);
        // then
        assertThrows(RuntimeException.class, () -> testBySignIn());
    }

    @Test
    void resetPasswordSendMsg() {
        // given
        UserResponse response = testBySignIn();
        String email = response.getEmail();

        UserPasswordResetMailRequest request = new UserPasswordResetMailRequest(email);

        // when
        userService.resetPasswordSendMsg(request);

        // then
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 회원이 없습니다."));

        assertNotNull(user.getResetPasswordKey());
    }

    @Test
    void withdraw() {
        // given
        httpServletRequest.addHeader("Authorization", "bearer " + getTestUserByToken());
        // when
        userService.withdraw(httpServletRequest);
        // then
        assertThrows(RuntimeException.class, () -> testBySignIn());
    }

    @Test
    void updatePassword() {
        // given
        UserResponse response = testBySignIn();
        httpServletRequest.addHeader("Authorization", "bearer " + response.getAccessToken()
        );

        PasswordInitRequest request = new PasswordInitRequest("1234", "12345");

        // when
        userService.updatePassword(request, httpServletRequest);

        // then
        assertThrows(RuntimeException.class, () -> testBySignIn());
    }

    private UserResponse testBySignIn() {
        UserRequest.SignIn signInRequest = UserRequest.SignIn.builder()
                .email("test100@test.com")
                .password("1234")
                .build();
        UserResponse user = userService.signIn(signInRequest);

        return user;
    }


    private String getTestUserByToken() {
        UserResponse response = testBySignIn();
        return response.getAccessToken();
    }
}