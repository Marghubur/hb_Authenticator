package com.hiringbell.authenticator.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User {

    @Id
    Long userId;
    String firstName;

    String lastName;

    String fatherName;

    String motherName;

    String email;

    String mobile;

    String alternateNumber;

    String address;

    String city;

    String state;

    String country;

    int roleId;

    int jobCategoryId;

    String categoryTypeIds;

    String jobLocationIds;

    int designationId;

    int pinCode;

    long reporteeId;

    @JsonProperty("isActive")
    boolean isActive;

    String followers;

    String friends;

    Long createdBy;

    Long updatedBy;

    String gender;

    String imageURL;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    Date createdOn;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    Date updatedOn;

    @Transient
    @JsonProperty("token")
    String token;

    @Transient
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    Date tokenExpiryDuration;

    String subscriber;

    @Transient
    String deviceId;

}
