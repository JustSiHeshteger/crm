package com.crmsystem.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CrmUtils {
    private CrmUtils() {

    }

    public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus){
        return new ResponseEntity<String>("{\"message\":\""+responseMessage+"\"}", httpStatus);
    }
}
