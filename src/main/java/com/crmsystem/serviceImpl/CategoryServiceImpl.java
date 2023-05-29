package com.crmsystem.serviceImpl;

import com.crmsystem.constants.CrmConstants;
import com.crmsystem.dao.CategoryDao;
import com.crmsystem.jwt.JwtFilter;
import com.crmsystem.model.Category;
import com.crmsystem.service.CategoryService;
import com.crmsystem.utils.CrmUtils;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    private boolean validateCategoryMap(Map<String, String> requestMap, boolean validateId) {
        if (requestMap.containsKey("name")) {
            if (requestMap.containsKey("id") && validateId) {
                return true;
            } else if (!validateId) {
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

    @Override
    public ResponseEntity<List<Category>> getAllCategory(String value) {
        try {
            if (!Strings.isNullOrEmpty(value) && value.equalsIgnoreCase("true")) {
                log.info("Inside if");
                return new ResponseEntity<>(categoryDao.getAllCategory(), HttpStatus.OK);
            }
            return new ResponseEntity<>(categoryDao.findAll(), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                if (validateCategoryMap(requestMap, true)) {
                    Optional optional = categoryDao.findById(Integer.parseInt(requestMap.get("id")));

                    if (!optional.isEmpty()) {
                        categoryDao.save(getCategoryFromMap(requestMap, true));
                        return CrmUtils.getResponseEntity("Category updated successfully.", HttpStatus.OK);
                    } else {
                        return CrmUtils.getResponseEntity("Category id doesn't exist.", HttpStatus.OK);
                    }
                }

                return CrmUtils.getResponseEntity(CrmConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            } else {
                return CrmUtils.getResponseEntity(CrmConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return CrmUtils.getResponseEntity(CrmConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
