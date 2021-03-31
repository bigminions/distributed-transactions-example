package com.example.bill.dao;

import com.example.bill.entity.Bill;
import org.springframework.data.repository.CrudRepository;

public interface AccountDao extends CrudRepository<Bill, Integer> {


}
