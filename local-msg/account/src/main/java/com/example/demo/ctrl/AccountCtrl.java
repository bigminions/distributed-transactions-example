package com.example.demo.ctrl;

import com.example.demo.dao.AccountDao;
import com.example.demo.entity.Account;
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
    ResponseEntity<List<Account>> listAll() {
        List<Account> list = new ArrayList<>();
        accountDao.findAll().forEach(list::add);
        return ResponseEntity.ok(list);
    }
}
