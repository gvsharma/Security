package com.security.security.Controllers;

import com.security.security.converters.UserObjectConverter;
import com.security.security.services.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsersController {
    private final UserService users;
    private final UserObjectConverter converter;

    public UsersController(UserService users, UserObjectConverter converter) {
        this.users = users;
        this.converter = converter;
    }


}
