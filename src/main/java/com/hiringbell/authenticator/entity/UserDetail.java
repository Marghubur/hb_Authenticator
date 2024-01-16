package com.hiringbell.authenticator.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="user_detail")
public class UserDetail {

    @Id
    @Column(name = "UserId")
    long userId;

    @Column(name = "PAN")
    String pan;

    @Column(name = "Aadhar")
    String aadhar;

    @Column(name = "PassportNumber")
    String passportNumber;

    @Column(name = "BankName")
    String bankName;

    @Column(name = "AccountNo")
    String accountNo;

    @Column(name = "Branch")
    String branch;

    @Column(name = "IFSCCode")
    String IfscCode;

    @Column(name = "JobTypeId")
    int jobTypeId;

    @Column(name = "ExperienceInMonths")
    int experienceInMonths;

    @Column(name = "lastCompanyName")
    String LastCompanyName;

    @Column(name = "LastWorkingDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date lastWorkingDate;

    @Column(name = "Designation")
    String designation;

    @Column(name = "Salary")
    BigDecimal salary;

    @Column(name = "ExpectedSalary")
    BigDecimal expectedSalary;

    @Column(name = "ExpectedDesignation")
    String expectedDesignation;

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
