package com.hiringbell.authenticator.repository;

import com.hiringbell.authenticator.entity.UserMedicalDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserMedicalDetailRepository extends JpaRepository<UserMedicalDetail, Long> {

    @Query(value = "select emd.* from user_medical_detail emd order by emd.UserId desc limit 1", nativeQuery = true)
    UserMedicalDetail getLastUerMedicalDetailRecord();

    @Query(nativeQuery = true, value = "select emd.* from user_medical_detail emd where emd.UserId = :userId")
    UserMedicalDetail getUserMedicalDetailByEmployeeId(@Param("userId") long userId);
}
