package com.example.bill.ctrl;

import com.example.bill.entity.Bill;
import com.example.bill.dao.AccountDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountCtrl {

    @Autowired
    AccountDao accountDao;

    @GetMapping(path = "/all")
    ResponseEntity<List<Bill>> listAll() {
        List<Bill> list = new ArrayList<>();
        accountDao.findAll().forEach(list::add);
        return ResponseEntity.ok(list);
    }
}
