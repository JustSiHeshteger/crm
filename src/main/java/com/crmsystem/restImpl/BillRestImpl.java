package com.crmsystem.restImpl;

import com.crmsystem.constants.CrmConstants;
import com.crmsystem.rest.BillRest;
import com.crmsystem.service.BillService;
import com.crmsystem.utils.CrmUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class BillRestImpl implements BillRest {

    BillService billService;

    @Autowired
    public BillRestImpl(BillService billService) {
        this.billService = billService;
    }


    @Override
    public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {
        try {
            return billService.generateReport(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CrmUtils.getResponseEntity(CrmConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
