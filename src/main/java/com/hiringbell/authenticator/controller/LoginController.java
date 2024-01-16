package com.hiringbell.authenticator.controller;

import com.hiringbell.authenticator.contract.ILoginService;
import com.hiringbell.authenticator.entity.Login;
import com.hiringbell.authenticator.model.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hb/api/oauth")
public class LoginController {
    @Autowired
    ILoginService loginService;

    @PostMapping("/googlelogin")
    public ResponseEntity<ApiResponse> registerWithGoogle(@RequestBody Login user) throws Exception {
        var result = loginService.userAuthetication(user);
        return ResponseEntity.ok(ApiResponse.Ok(result));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<ApiResponse> authenticate(@RequestBody Login login) throws Exception {
        var result = loginService.authenticateUserService(login);
        return ResponseEntity.ok(ApiResponse.Ok(result));
    }
}