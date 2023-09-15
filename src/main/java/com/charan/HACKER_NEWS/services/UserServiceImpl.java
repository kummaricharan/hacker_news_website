package com.charan.HACKER_NEWS.services;

import com.charan.HACKER_NEWS.entity.User;
import com.charan.HACKER_NEWS.repository.UserRepository;
import org.springframework.stereotype.Repository;

@Repository
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public User findByName(String username) {
        return userRepository.findByName(username);
    }
}
