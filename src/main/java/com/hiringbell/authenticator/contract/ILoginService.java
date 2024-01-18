package com.hiringbell.authenticator.contract;

import com.hiringbell.authenticator.entity.Login;
import com.hiringbell.authenticator.model.LoginResponse;
import org.springframework.stereotype.Component;

@Component
public interface ILoginService {
    LoginResponse userAuthetication(Login user) throws Exception;
    LoginResponse authenticateUserService(Login login) throws Exception;
    public String signupService(Login login);
}
