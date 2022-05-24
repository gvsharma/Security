package com.security.security.Controllers;

import com.security.security.converters.UserObjectConverter;
import com.security.security.dtos.requests.UserUpdateRequest;
import com.security.security.dtos.responses.UserProfileResponse;
import com.security.security.dtos.responses.UserResponse;
import com.security.security.entities.ErrorEntity;
import com.security.security.entities.UserEntity;
import com.security.security.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
public class UsersController {
    private final UserService users;
    private final UserObjectConverter converter;

    public UsersController(UserService users, UserObjectConverter converter) {
        this.users = users;
        this.converter = converter;
    }

    @GetMapping("/user")
    ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal UserEntity userEntity) {
        return ResponseEntity.ok(converter.entityToResponse(userEntity));
    }

    @PutMapping("/user")
    ResponseEntity<UserResponse> updateUser(
            @AuthenticationPrincipal UserEntity userEntity,
            @RequestBody UserUpdateRequest body
    ) {
        if (body.getUser().getUsername() != null) userEntity.setUsername(body.getUser().getUsername());
        if (body.getUser().getBio() != null) userEntity.setBio(body.getUser().getBio());
        if (body.getUser().getImage() != null) userEntity.setImage(body.getUser().getImage());
        if (body.getUser().getEmail() != null) userEntity.setEmail(body.getUser().getEmail());
        userEntity.setPassword(body.getUser().getPassword());

        return new ResponseEntity<>(
                converter.entityToResponse(users.updateUser(userEntity)),
                HttpStatus.ACCEPTED
        );
    }

    @GetMapping("/profiles/{username}")
    ResponseEntity<UserProfileResponse> getUserProfile(@PathVariable("username") String username) {
        return ResponseEntity.ok(
                converter.entityToUserProfileResponse(users.findUserByUsername(username))
        );
    }

    @PostMapping("/profiles/{username}/follow")
    ResponseEntity<UserProfileResponse> followUser(
            @PathVariable("username") String username,
            @AuthenticationPrincipal UserEntity userEntity
    ) {
        UserEntity response = users.followUser(username, userEntity.getId());
        return ResponseEntity.ok(converter.entityToUserProfileResponse(response));
    }

    @DeleteMapping("/profiles/{username}/follow")
    ResponseEntity<UserProfileResponse> unfollowUser(
            @PathVariable("username") String username,
            @AuthenticationPrincipal UserEntity userEntity
    ) {
        UserEntity response = users.unfollowUser(username, userEntity.getId());
        return ResponseEntity.ok(converter.entityToUserProfileResponse(response));
    }


    @ExceptionHandler({RuntimeException.class})
    ResponseEntity<ErrorEntity> handleExceptions(RuntimeException exception) {
        String message = exception.getMessage();
        HttpStatus errorStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        if (exception instanceof UserService.UserNotFoundException) {
            errorStatus = HttpStatus.NOT_FOUND;
        }
        if (exception instanceof UserService.UsernameConflictException) {
            errorStatus = HttpStatus.CONFLICT;
        }
        if (exception.toString().contains("DataIntegrityViolationException") &&
                exception.toString().contains("pkey")) {
            message = "Data Already Exists";
            errorStatus = HttpStatus.CONFLICT;
        }

        return new ResponseEntity<>(
                ErrorEntity.from(message), errorStatus
        );
    }


}
