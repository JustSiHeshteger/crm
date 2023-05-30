package com.crmsystem.restImpl;

import com.crmsystem.rest.DashboardRest;
import com.crmsystem.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DashboardRestImpl implements DashboardRest {

    DashboardService dashboardService;

    @Autowired
    public DashboardRestImpl(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @Override
    public ResponseEntity<Map<String, Object>> getCount() {
        return dashboardService.getCount();
    }
}
