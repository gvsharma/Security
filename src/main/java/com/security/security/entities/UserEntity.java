package com.security.security.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity extends BaseEntity{
    private String email;
    private String password;
    private String bio;
    private String image;
    private String username;

    private Set<ArticleEntity> favorite;
    private Set<UserEntity> followers;

    @ManyToMany(mappedBy = "fans", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public Set<ArticleEntity> getFavorite() {
        return favorite;
    }

    @ManyToMany
    @JoinTable(name = "user_followers")
    private Set<UserEntity> getFollowers() {
        return followers;
    }
}
