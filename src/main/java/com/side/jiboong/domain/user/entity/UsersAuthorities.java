package com.side.jiboong.domain.user.entity;

import com.side.jiboong.domain.BaseEntity;
import com.side.jiboong.domain.user.UserRoleType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_authorities")
public class UsersAuthorities extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRoleType roleType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    private UsersAuthorities(UserRoleType roleType, User user) {
        this.roleType = roleType;
        this.user = user;
    }

    public static UsersAuthorities of(User user, UserRoleType roleType) {
        return UsersAuthorities.builder()
                .user(user)
                .roleType(roleType)
                .build();
    }
}
