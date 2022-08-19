package com.example.hellpyending.user.entity;


import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(indexes = {
        @Index(columnList = "userType")
})
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType userType;

    @Column(length = 400, nullable = false, unique = true)
    private String email;

    @Column
    private String password;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 30, nullable = false, unique = true)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column
    private Sex sex;

    private LocalDate birthday;

    // enum 표현하는 개선이 필요해보임.
    @Column(name = "delete_yn")
    private char deleteYn;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false, updatable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime updatedAt;

    // 광역시
    // ex) 인천, 서울, 부산 ...
    @Column(nullable = false)
    private String address_1st;

    // 시군구
    // ex) 강화군, 서구, 중구, 미추홀구 ...
    @Column(nullable = false)
    private String address_2st;

    // 동읍면리
    // ex) 간석동, 신현동, 관청리, 화정동 ...
    @Column(nullable = false)
    private String address_3st;

    // 도로명
    // ex) 인천 서구 봉오재1로 36, 경기 고양시 덕양구 수원문산고속도로 51, 인천 부평구 평천로 559-3 ...
    @Column(nullable = false)
    private String address_4st;

    // 상세 주소
    // ex) 905동 403호, 101동 1301호 ...
    @Column(nullable = false)
    private String address_detail;

    public Users(String name, String password, List<GrantedAuthority> authorities) {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Users user)) return false;
        return id != null && id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
