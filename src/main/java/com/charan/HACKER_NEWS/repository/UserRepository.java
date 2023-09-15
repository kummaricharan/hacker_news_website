package com.charan.HACKER_NEWS.repository;

import com.charan.HACKER_NEWS.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User,Integer> {
    @Query("SELECT user FROM User user WHERE user.username = :username")
    User findByName(@Param("username") String username);

}
