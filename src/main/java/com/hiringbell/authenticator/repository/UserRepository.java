package com.hiringbell.authenticator.repository;

import com.hiringbell.authenticator.entity.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Integer> {
    @Query(nativeQuery = true, value = "select u.* from user u where u.Email = :email")
    UserEntity findByEmail(@Param("email")String email);
}
