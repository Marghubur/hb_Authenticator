package com.hiringbell.authenticator.repository;

import com.hiringbell.authenticator.entity.EmployeeMedicalDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeMedicalDetailRepository extends JpaRepository<EmployeeMedicalDetail, Long> {

    @Query(value = "select emd.* from employee_medical_detail emd order by emd.EmployeeId desc limit 1", nativeQuery = true)
    EmployeeMedicalDetail getLastEmployeeMedicalDetailRecord();

    @Query(nativeQuery = true, value = "select emd.* from employee_medical_detail emd where emd.EmployeeId = :employeeId")
    EmployeeMedicalDetail getEmployeeMedicalDetailByEmployeeId(@Param("employeeId") long employeeId);
}
