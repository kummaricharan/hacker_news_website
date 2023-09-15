package com.charan.HACKER_NEWS.services;

import com.charan.HACKER_NEWS.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserService {
    void save(User user);
    User findByName(@Param("username") String username);
}
