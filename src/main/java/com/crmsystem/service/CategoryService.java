package com.crmsystem.service;

import com.crmsystem.model.Category;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface CategoryService {

    ResponseEntity<String> addNewCategory(Map<String, String> requestMap);

    ResponseEntity<List<Category>> getAllCategory(String value);

    ResponseEntity<String> updateCategory(Map<String, String> requestMap);
}
