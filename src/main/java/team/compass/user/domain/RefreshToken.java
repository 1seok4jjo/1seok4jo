package team.compass.user.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "refresh_token")
public class RefreshToken {
    @Id
    private String email;
    @Column(nullable = false)
    private String refreshToken;

//    @OneToOne(mappedBy = "refresh_token")
//    private User member;

    public void updateValue(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Builder
    public RefreshToken(String email, String refreshToken) {
        this.email = email;
        this.refreshToken = refreshToken;
    }
}
