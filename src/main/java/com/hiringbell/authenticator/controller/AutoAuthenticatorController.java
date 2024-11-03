package com.hiringbell.authenticator.controller;

import com.hiringbell.authenticator.contract.ILoginService;
import com.hiringbell.authenticator.entity.User;
import com.hiringbell.authenticator.model.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hb/api/auto")
public class AutoAuthenticatorController {
    @Autowired
    ILoginService loginService;

    @PostMapping("/validateUserWithToken")
    public ResponseEntity<ApiResponse> autoAuthenticate(@RequestBody User user) throws Exception {
        var result = loginService.autoAuthentication(user);
        return ResponseEntity.ok(ApiResponse.Ok(result));
    }
}
