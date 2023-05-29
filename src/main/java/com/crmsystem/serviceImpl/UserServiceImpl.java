package com.crmsystem.serviceImpl;

import com.crmsystem.constants.CrmConstants;
import com.crmsystem.dao.UserDao;
import com.crmsystem.jwt.CustomerUserDetailService;
import com.crmsystem.jwt.JwtFilter;
import com.crmsystem.jwt.JwtUtil;
import com.crmsystem.model.User;
import com.crmsystem.service.UserService;
import com.crmsystem.utils.CrmUtils;
import com.crmsystem.wrapper.UserWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    UserDao userDao;
    AuthenticationManager authenticationManager;
    CustomerUserDetailService customerUserDetailService;
    JwtUtil jwtUtil;
    JwtFilter jwtFilter;

    @Autowired
    public UserServiceImpl(UserDao userDao, AuthenticationManager authenticationManager,
                           CustomerUserDetailService customerUserDetailService, JwtUtil jwtUtil,
                           JwtFilter jwtFilter) {
        this.userDao = userDao;
        this.authenticationManager = authenticationManager;
        this.customerUserDetailService = customerUserDetailService;
        this.jwtUtil = jwtUtil;
        this.jwtFilter = jwtFilter;
    }

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Inside signup {}", requestMap);
        try {
            if (validateSignUpMap(requestMap)) {
                User user = userDao.findByEmailId(requestMap.get("email"));
                if (Objects.isNull(user)) {
                    userDao.save(getUserFromMap(requestMap));
                    return CrmUtils.getResponseEntity("Successfully Registered", HttpStatus.OK);
                } else {
                    return CrmUtils.getResponseEntity("Email already exists.", HttpStatus.BAD_REQUEST);
                }
            } else {
                return CrmUtils.getResponseEntity(CrmConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CrmUtils.getResponseEntity(CrmConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateSignUpMap(Map<String, String> requestMap) {
        if (requestMap.containsKey("name") && requestMap.containsKey("contactNumber")
                && requestMap.containsKey("email") && requestMap.containsKey("password")) {
            return true;
        } else {
            return false;
        }
    }

    private User getUserFromMap(Map<String, String> requestMap) {
        User user = new User();
        user.setName(requestMap.get("name"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus("false");
        user.setRole("user");
        return user;
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Inside login");
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password"))
            );

            if (authentication.isAuthenticated()) {
                if (customerUserDetailService.getUserDetail().getStatus().equalsIgnoreCase("true")) {
                    return new ResponseEntity<String>("{\"token\":\"" +
                            jwtUtil.generateToken(customerUserDetailService.getUserDetail().getEmail(),
                                    customerUserDetailService.getUserDetail().getRole()) + "\"}", HttpStatus.OK);
                } else {
                    return new ResponseEntity<String>("{\"message\":\"" + "Wait for admin approval." + "\"}", HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception ex) {
            log.error("{}", ex);
        }

        return new ResponseEntity<String>("{\"message\":\"" + "Bad Credentials" + "\"}", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        try {

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}