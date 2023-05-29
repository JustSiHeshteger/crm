package com.crmsystem.dao;

import com.crmsystem.model.User;
import com.crmsystem.wrapper.UserWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserDao extends JpaRepository<User, Long> {
    User findByEmailId(@Param("email") String email);

    List<UserWrapper> getAllUser();
}
