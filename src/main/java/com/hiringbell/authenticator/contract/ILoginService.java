package com.hiringbell.authenticator.contract;

import com.hiringbell.authenticator.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public interface ILoginService {
    UserEntity userAuthetication(UserEntity user);
}
