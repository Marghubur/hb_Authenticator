package com.hiringbell.authenticator.repository;

import com.hiringbell.authenticator.entity.EmployeeDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeDetailRepository extends JpaRepository<EmployeeDetail, Long> {

    @Query(nativeQuery = true, value = "select ed.* from employee_detail ed where ed.EmployeeId = :employeeId")
    EmployeeDetail getEmployeeDetailByEmployeeId(@Param("employeeId") long employeeId);

}
