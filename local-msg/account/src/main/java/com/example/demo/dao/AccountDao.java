package com.example.demo.dao;

import com.example.demo.entity.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountDao extends CrudRepository<Account, Integer> {


}
