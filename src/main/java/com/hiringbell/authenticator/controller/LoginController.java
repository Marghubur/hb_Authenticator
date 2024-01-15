package com.hiringbell.authenticator.controller;

import com.hiringbell.authenticator.contract.ILoginService;
import com.hiringbell.authenticator.entity.UserEntity;
import com.hiringbell.authenticator.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/hb/api/oauth")
public class LoginController {
    @Autowired
    ILoginService loginService;

    @PostMapping("/googlelogin")
    public ResponseEntity<UserEntity> registerWithGoogle(@RequestBody UserEntity user) {
        UserEntity userEntity = loginService.userAuthetication(user);
        return ResponseEntity.ok(userEntity);
    }
}