package com.mango.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
@Table(name = "User")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    //mappedBy 쓸때는 지정한 변수명
    @OneToMany(mappedBy = "user")
    private List<ReviewLike> reviewLikeList;

    @OneToMany(mappedBy = "user")
    private List<SearchLog> searchLogList;

    @Builder
    public User(Long id, String username, String password, List<ReviewLike> reviewLikeList, List<SearchLog> searchLogList) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.reviewLikeList = reviewLikeList;
        this.searchLogList = searchLogList;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
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
        return false;
    }
}
