package com.hiringbell.authenticator.service;

import com.hiringbell.authenticator.contract.ILoginService;
import com.hiringbell.authenticator.entity.UserEntity;
import com.hiringbell.authenticator.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LoginService implements ILoginService {
    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    public UserEntity userAuthetication(UserEntity user) {
        Map<String, Object> result = jwtUtil.validateToken(user.getToken());

        return null;
    }
}
