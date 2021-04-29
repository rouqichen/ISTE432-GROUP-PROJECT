package com.group.MediaLibrary.service;

import com.group.MediaLibrary.business.User;
import com.group.MediaLibrary.service.request.UserRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String login(@RequestBody UserRequest user) {
        return User.login(user.getUsername(), user.getPassword());
    }

}
