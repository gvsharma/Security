package com.security.security.converters;

import com.security.security.dtos.responses.Profile;
import com.security.security.dtos.responses.UserProfileResponse;
import com.security.security.dtos.responses.UserResponse;
import com.security.security.entities.UserEntity;
import com.security.security.services.JwtService;
import org.springframework.stereotype.Service;

@Service
public class UserObjectConverter {
    private JwtService jwtService;
    public UserObjectConverter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public UserResponse entityToResponse(UserEntity userEntity) {
        return UserResponse.fromUserEntity(userEntity, jwtService.createJwt(userEntity));
    }

    public UserProfileResponse entityToUserProfileResponse(UserEntity userEntity) {
        return UserProfileResponse.builder()
                .profile(Profile.builder()
                        .bio(userEntity.getBio())
                        .username(userEntity.getUsername())
                        .image(userEntity.getImage())
                        .email(userEntity.getEmail())
                        .build())
                .build();
    }
}
