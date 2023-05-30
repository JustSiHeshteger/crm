package com.crmsystem.serviceImpl;

import com.crmsystem.constants.CrmConstants;
import com.crmsystem.dao.ProductDao;
import com.crmsystem.jwt.JwtFilter;
import com.crmsystem.model.Category;
import com.crmsystem.model.Product;
import com.crmsystem.service.ProductService;
import com.crmsystem.utils.CrmUtils;
import com.crmsystem.wrapper.ProductWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    ProductDao productDao;

    JwtFilter jwtFilter;

    @Autowired
    public ProductServiceImpl(ProductDao productDao, JwtFilter jwtFilter) {
        this.productDao = productDao;
        this.jwtFilter = jwtFilter;
    }

    @Override
    public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                if (validateProductMap(requestMap, false)) {
                    productDao.save(getProductFromMap(requestMap, false));
                    return CrmUtils.getResponseEntity("Product Added Successfully.", HttpStatus.OK);
                }

                return CrmUtils.getResponseEntity(CrmConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            } else {
                return CrmUtils.getResponseEntity(CrmConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {

        }

        return CrmUtils.getResponseEntity(CrmConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateProductMap(Map<String, String> requestMap, boolean validateId) {
        if (requestMap.containsKey("name")) {
            if (requestMap.containsKey("id") && validateId) {
                return true;
            } else if (!validateId) {
                return true;
            }
        }

        return false;
    }

    private Product getProductFromMap(Map<String, String> requestMap, boolean isAdd) {
        Category category = new Category();
        category.setId(Integer.parseInt(requestMap.get("categoryId")));

        Product product = new Product();
        if (isAdd) {
            product.setId(Integer.parseInt(requestMap.get("id")));
        } else {
            product.setStatus("true");
        }

        product.setCategory(category);
        product.setName(requestMap.get("name"));
        product.setDescription((requestMap.get("description")));
        product.setPrice(Integer.parseInt(requestMap.get("price")));
        return product;
    }


    @Override
    public ResponseEntity<List<ProductWrapper>> getAllProduct() {
        try {
             return new ResponseEntity<>(productDao.getAllProduct(), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                if (validateProductMap(requestMap, true)) {
                    Optional<Product> optional = productDao.findById(Integer.parseInt(requestMap.get("id")));

                    if (!optional.isEmpty()) {
                        Product product = getProductFromMap(requestMap, true);
                        product.setStatus(optional.get().getStatus());
                        productDao.save(product);
                        return CrmUtils.getResponseEntity("Product updated successfully.", HttpStatus.OK);
                    } else {
                        return CrmUtils.getResponseEntity("Product id doesn't exist.", HttpStatus.OK);
                    }
                } else {
                    return CrmUtils.getResponseEntity(CrmConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
                }
            } else {
                return CrmUtils.getResponseEntity(CrmConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return CrmUtils.getResponseEntity(CrmConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteProduct(Integer id) {
        try {
            if (jwtFilter.isAdmin()) {
                Optional<Product> optional = productDao.findById(id);
                if (!optional.isEmpty()) {
                    productDao.deleteById(id);
                    return CrmUtils.getResponseEntity("Product deleted successfully.", HttpStatus.OK);
                }

                return CrmUtils.getResponseEntity("Product id doesn't exist.", HttpStatus.OK);
            } else {
                return CrmUtils.getResponseEntity(CrmConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return CrmUtils.getResponseEntity(CrmConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                Optional<Product> optional =  productDao.findById(Integer.parseInt(requestMap.get("id")));

                if (!optional.isEmpty()) {
                    productDao.updateProductStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
                    return CrmUtils.getResponseEntity("Product status updated successfully.", HttpStatus.OK);
                }

                return CrmUtils.getResponseEntity("Product id doesn't exist.", HttpStatus.OK);
            } else {
                return CrmUtils.getResponseEntity(CrmConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return CrmUtils.getResponseEntity(CrmConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
