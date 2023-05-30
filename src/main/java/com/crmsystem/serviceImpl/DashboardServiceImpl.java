package com.crmsystem.serviceImpl;

import com.crmsystem.dao.BillDao;
import com.crmsystem.dao.CategoryDao;
import com.crmsystem.dao.ProductDao;
import com.crmsystem.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    CategoryDao categoryDao;
    ProductDao productDao;
    BillDao billDao;

    @Autowired
    public DashboardServiceImpl(CategoryDao categoryDao, ProductDao productDao, BillDao billDao) {
        this.categoryDao = categoryDao;
        this.productDao = productDao;
        this.billDao = billDao;
    }

    @Override
    public ResponseEntity<Map<String, Object>> getCount() {
        Map<String, Object> map = new HashMap<>();
        map.put("category", categoryDao.count());
        map.put("product", productDao.count());
        map.put("bill", billDao.count());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
