package com.side.jiboong.domain.user.entity;

import com.side.jiboong.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.PERSIST;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Table(name = "users")
@Entity
@NoArgsConstructor(access = PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column
    private ZonedDateTime lastLogin;

    @Column
    private Integer loginCount = 0;

    @OneToMany(cascade = PERSIST, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    @OrderBy("roleType ASC")
    private final List<UsersAuthorities> authorities = new ArrayList<>();

    public User(
            String username,
            String password,
            ZonedDateTime lastLogin,
            Integer loginCount
    ) {
        this.username = username;
        this.password = password;
        this.lastLogin = lastLogin;
        this.loginCount = loginCount;
    }

    public void login() {
        this.lastLogin = ZonedDateTime.now();
        this.loginCount++;
    }
}
