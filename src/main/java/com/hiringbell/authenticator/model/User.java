package com.hiringbell.authenticator.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    Long userId;
    String mobile;
    private String email;
    private String name;
}
