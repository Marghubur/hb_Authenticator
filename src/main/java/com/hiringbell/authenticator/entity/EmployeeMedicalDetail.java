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
@Table(name="employee_medical_detail")
public class EmployeeMedicalDetail {

    @Id
    @Column(name = "EmployeeMedicalDetailId")
    long employeeMedicalDetailId;

    @Column(name = "EmployeeId")
    long employeeId;

    @Column(name = "MedicalConsultancyId")
    long medicalConsultancyId;

    @Column (name = "ConsultedBy")
    String ConsultedBy;

    @Column(name = "ConsultedOn")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date consultedOn;

    @Column(name = "ReferenceId")
    long referenceId;

    @Column(name = "ReportId")
    long reportId;

    @Column(name = "ReportPath")
    String ReportPath;

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
