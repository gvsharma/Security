package com.security.security.services;

import com.security.security.entities.UserEntity;
import com.security.security.repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepo;
    private final BCryptPasswordEncoder bcryptEncoder;

    public UserService(UserRepository userRepo, BCryptPasswordEncoder bcryptEncoder) {
        this.userRepo = userRepo;
        this.bcryptEncoder = bcryptEncoder;
    }
    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException() {
            super("No such user found");
        }
    }

    public static class UserPasswordIncorectException extends SecurityException {
        public UserPasswordIncorectException() {
            super("Invalid Password");
        }
    }

    public static class UsernameConflictException extends SecurityException {
        public UsernameConflictException() {
            super("Username already exists");
        }
    }

    @Bean
    public static BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public UserEntity registerNewuser(String username, String password, String email){
        UserEntity user = userRepo.findUserEntityByUsername(username);
        if(user != null){
            throw new UsernameConflictException();
        }

        UserEntity userEntity = UserEntity.builder()
                .username(username)
                .password(bcryptEncoder.encode(password))
                .email(email).build();
        return userRepo.save(userEntity);
    }

    public UserEntity verifyUser(String email, String password) {
        UserEntity user = userRepo.findUserEntityByEmail(email);
        if (user == null)
            throw new UserNotFoundException();

        if (!bcryptEncoder.matches(password, user.getPassword()))
            throw new UserPasswordIncorectException();

        return user;
    }

    public UserEntity updateUser(UserEntity userEntity) {
        if (userEntity.getPassword() != null) {
            userEntity.setPassword(bcryptEncoder.encode(userEntity.getPassword()));
        }
        return userRepo.save(userEntity);
    }

    public UserEntity findUserByUsername(String username) {
        UserEntity user = userRepo.findUserEntityByUsername(username);
        if (user == null) throw new UserNotFoundException();
        return user;
    }

    public UserEntity followUser(String username, Long loggedInUserUserId) {
        UserEntity userToFollow = findUserByUsername(username);
        userRepo.followUser(loggedInUserUserId, userToFollow.getId());
        return userToFollow;
    }

    public UserEntity unfollowUser(String username, Long loggedInUserUserId) {
        UserEntity userToUnfollow = findUserByUsername(username);
        userRepo.unfollowUser(loggedInUserUserId, userToUnfollow.getId());
        return userToUnfollow;
    }

}
