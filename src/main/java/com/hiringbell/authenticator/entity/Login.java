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
    @Column(name = "LoginId")
    @JsonProperty("LoginId")
    Long loginId;

    @Column(name = "UserId")
    @JsonProperty("UserId")
    Long userId;

    @Column(name = "Email")
    @JsonProperty("Email")
    String email;

    @Column(name = "Mobile")
    @JsonProperty("Mobile")
    String mobile;

    @Column(name = "Password")
    @JsonProperty("Password")
    String password;


    @Column(name = "RoleId")
    @JsonProperty("RoleId")
    int roleId;

    @Column(name = "IsActive")
    @JsonProperty("IsActive")
    boolean isActive;

    @Column(name = "CreatedBy")
    @JsonProperty("CreatedBy")
    Long createdBy;

    @Column(name = "UpdatedBy")
    @JsonProperty("UpdatedBy")
    Long updatedBy;

    @Column(name = "CreatedOn")
    @JsonProperty("CreatedOn")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date createdOn;

    @Column(name = "UpdatedOn")
    @JsonProperty("UpdatedOn")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date updatedOn;

    @Transient
    @JsonProperty("Token")
    String token;

    @Transient
    @JsonProperty("TokenExpiryDuration")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date tokenExpiryDuration;

    @Transient
    @JsonProperty("FullName")
    String fullName;
}
