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
@Table(name="user")
public class User {

        @Id
        @Column(name = "UserId")
        @JsonProperty("UserId")
        Long userId;

        @Column(name = "FirstName")
        @JsonProperty("FirstName")
        String firstName;

        @Column(name = "LastName")
        @JsonProperty("LastName")
        String lastName;

        @Column(name = "FatherName")
        @JsonProperty("FatherName")
        String fatherName;

        @Column(name = "MotherName")
        @JsonProperty("MotherName")
        String motherName;

        @Column(name = "Email")
        @JsonProperty("Email")
        String email;

        @Column(name = "Mobile")
        @JsonProperty("Mobile")
        String mobile;

        @Column(name = "AlternateNumber")
        @JsonProperty("AlternateNumber")
        String alternateNumber;

        @Column(name = "Address")
        @JsonProperty("Address")
        String address;

        @Column(name = "City")
        @JsonProperty("City")
        String city;

        @Column(name = "State")
        @JsonProperty("State")
        String state;

        @Column(name = "Country")
        @JsonProperty("Country")
        String country;

        @Column(name = "RoleId")
        @JsonProperty("RoleId")
        int roleId;

        @Column(name = "JobCategoryId")
        @JsonProperty("JobCategoryId")
        int jobCategoryId;

        @Column(name = "CategoryTypeIds")
        @JsonProperty("CategoryTypeIds")
        String categoryTypeIds;

        @Column(name = "JobLocationIds")
        @JsonProperty("JobLocationIds")
        String jobLocationIds;

        @Column(name = "DesignationId")
        @JsonProperty("DesignationId")
        int designationId;

        @Column(name = "PinCode")
        @JsonProperty("PinCode")
        int pinCode;

        @Column(name = "ReporteeId")
        @JsonProperty("ReporteeId")
        long reporteeId;

        @Column(name = "IsActive")
        @JsonProperty("IsActive")
        boolean isActive;

        @Column(name = "Followers")
        @JsonProperty("Followers")
        String followers;

        @Column(name = "Friends")
        @JsonProperty("Friends")
        String friends;

        @Column(name = "CreatedBy")
        @JsonProperty("CreatedBy")
        Long createdBy;

        @Column(name = "UpdatedBy")
        @JsonProperty("UpdatedBy")
        Long updatedBy;

        @Column(name = "CreatedOn")
        @JsonProperty("CreatedOn")
        @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
        Date createdOn;

        @Column(name = "UpdatedOn")
        @JsonProperty("UpdatedOn")
        @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
        Date updatedOn;

        @Transient
        @JsonProperty("Token")
        String token;

        @Transient
        @JsonProperty("TokenExpiryDuration")
        @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
        Date tokenExpiryDuration;
}
