package com.hiringbell.authenticator.repository;

import com.hiringbell.authenticator.entity.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserDetailRepository extends JpaRepository<UserDetail, Long> {

    @Query(nativeQuery = true, value = "select ed.* from user_detail ed where ed.UserId = :userId")
    UserDetail getEmployeeDetailByEmployeeId(@Param("userId") long userId);

}
