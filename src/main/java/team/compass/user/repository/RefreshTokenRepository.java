package team.compass.user.repository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;
import team.compass.user.domain.RefreshToken;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
public class RefreshTokenRepository {
    private RedisTemplate redisTemplate;
    private RedisTemplate redisBlackListTemplate;
    private long EXPIRE_TIME;

    public RefreshTokenRepository(
            RedisTemplate redisTemplate,
            @Value("${jwt.refresh-token-expire-time}") long EXPIRE_TIME
    ) {
        this.redisTemplate = redisTemplate;
        this.redisBlackListTemplate = redisTemplate;
        this.EXPIRE_TIME = EXPIRE_TIME;
    }

    public void save(RefreshToken refreshToken) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        valueOperations.set(refreshToken.getEmail(), refreshToken.getRefreshToken(), EXPIRE_TIME, TimeUnit.MILLISECONDS);
    }

    public void delete(RefreshToken refreshToken) {
        if(redisTemplate.opsForValue().get(refreshToken.getEmail()) != null) {
            redisTemplate.delete(refreshToken.getEmail());
        }
    }

    public void setBlackList(String key, Long milliSeconds) {
        redisBlackListTemplate.opsForValue().set(key, "logout", milliSeconds, TimeUnit.MILLISECONDS);
    }

    public boolean hasKeyBlackList(String key) {
        return redisBlackListTemplate.hasKey(key);
    }

    public Optional<RefreshToken> findByEmail(String email) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        String token = valueOperations.get(email);

        if(ObjectUtils.isEmpty(token)) {
            return Optional.empty();
        }

        return Optional.of(new RefreshToken(email, token));
    }
}
