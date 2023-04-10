package team.compass.member.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import team.compass.member.dto.MemberRequestDto;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member")
public class Member implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "userId", nullable = false)
    private Integer userId;

    @Column(name = "email")
    private String email;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "nickName")
    private String nickName;
    @Column(name = "introduction")
    private String introduction;
    @Column(name = "emailAuthKey")
    private String emailAuthKey;
    @Column(name = "resetPasswordKey")
    private String resetPasswordKey;
    @Column(name = "kakaoId")
    private String kakaoId;

    // 권한 추가?????
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();



    // post entity
    // @OneToMany(mappedBy = "member", fetch = FetchType.EAGER)
    // private List<Post> posts;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static Member from(MemberRequestDto.SignUp parameter) {
        return Member.builder()
                .email(parameter.getEmail().toLowerCase(Locale.ROOT))
                .password(parameter.getPassword())
                .nickName(parameter.getNickname())
                .build();
    }
}
