package com.crmsystem.serviceImpl;

import com.crmsystem.constants.CrmConstants;
import com.crmsystem.dao.CategoryDao;
import com.crmsystem.jwt.JwtFilter;
import com.crmsystem.model.Category;
import com.crmsystem.service.CategoryService;
import com.crmsystem.utils.CrmUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    CategoryDao categoryDao;

    JwtFilter jwtFilter;

    @Autowired
    public CategoryServiceImpl(CategoryDao categoryDao, JwtFilter jwtFilter) {
        this.categoryDao = categoryDao;
        this.jwtFilter = jwtFilter;
    }

    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                if (validateCategoryMap(requestMap, false)) {
                    categoryDao.save(getCategoryFromMap(requestMap, false));
                    return CrmUtils.getResponseEntity("Category added successfully.", HttpStatus.OK);
                }
            } else {
                return CrmUtils.getResponseEntity(CrmConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return CrmUtils.getResponseEntity(CrmConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateCategoryMap(Map<String, String> requestMap, boolean validate) {
        if (requestMap.containsKey("name")) {
            if (requestMap.containsKey("id") && validate) {
                return true;
            } else if (!validate) {
                return true;
            }
        }

        return false;
    }

    private Category getCategoryFromMap(Map<String, String> requestMap, Boolean isAdd) {
        Category category = new Category();
        if (isAdd) {
            category.setId(Integer.parseInt(requestMap.get("id")));
        }
        category.setName(requestMap.get("name"));
        return category;
    }
}
