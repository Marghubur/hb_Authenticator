package com.hiringbell.authenticator.repository;

import com.hiringbell.authenticator.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query(value = "select e.* from employee e order by e.EmployeeId desc limit 1", nativeQuery = true)
    Employee getLastEmployeeId();
}
