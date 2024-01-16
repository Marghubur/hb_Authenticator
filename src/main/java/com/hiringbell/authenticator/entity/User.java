package com.hiringbell.authenticator.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
        Long userId;

        @Column(name = "FirstName")
        String firstName;

        @Column(name = "LastName")
        String lastName;

        @Column(name = "FatherName")
        String fatherName;

        @Column(name = "MotherName")
        String motherName;

        @Column(name = "Email")
        String email;

        @Column(name = "Mobile")
        String mobile;

        @Column(name = "AlternateNumber")
        String alternateNumber;

        @Column(name = "Address")
        String address;

        @Column(name = "City")
        String city;

        @Column(name = "State")
        String state;

        @Column(name = "Country")
        String country;

        @Column(name = "RoleId")
        int roleId;

        @Column(name = "DesignationId")
        int designationId;

        @Column(name = "PinCode")
        int pinCode;

        @Column(name = "ReporteeId")
        long reporteeId;

        @Column(name = "IsActive")
        boolean isActive;

        @Column(name = "CreatedBy")
        Long createdBy;

        @Column(name = "UpdatedBy")
        Long updatedBy;

        @Column(name = "CreatedOn")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        Date createdOn;
        @Column(name = "UpdatedOn")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        Date updatedOn;
}
