package com.hiringbell.authenticator.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hiringbell.authenticator.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    @JsonProperty("UserDetail")
    UserEntity userDetail;
}
