package team.compass.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.compass.user.domain.SignUpInput;
import team.compass.user.domain.model.User;
import team.compass.user.domain.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class SignUpUserService {
    private final UserRepository userRepository;

    public User signUp(SignUpInput parameter) {
        return userRepository.save(User.from(parameter));
    }
}
