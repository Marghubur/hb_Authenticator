package com.hiringbell.authenticator.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name="login")
public class Login {
    @Id
    @Column(name = "loginId")
    @JsonProperty("loginId")
    Long loginId;

    @Column(name = "userId")
    @JsonProperty("userId")
    Long userId;

    @Column(name = "email")
    @JsonProperty("email")
    String email;

    @Column(name = "mobile")
    @JsonProperty("mobile")
    String mobile;

    @Column(name = "password")
    @JsonProperty("password")
    String password;

    @Column(name = "roleId")
    @JsonProperty("roleId")
    int roleId;

    @Column(name = "isActive")
    @JsonProperty("isActive")
    boolean isActive;

    @Column(name = "isAccountConfig")
    @JsonProperty("isAccountConfig")
    boolean isAccountConfig;

    @Column(name = "createdBy")
    @JsonProperty("createdBy")
    Long createdBy;

    @Column(name = "updatedBy")
    @JsonProperty("updatedBy")
    Long updatedBy;

    @Column(name = "createdOn")
    @JsonProperty("createdOn")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    Date createdOn;

    @Column(name = "updatedOn")
    @JsonProperty("updatedOn")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    Date updatedOn;

    @Transient
    @JsonProperty("fullName")
    String fullName;

    @Transient
    @JsonProperty("gender")
    String gender;

    @Column(name = "deviceId")
    @JsonProperty("deviceId")
    String deviceId;
}
