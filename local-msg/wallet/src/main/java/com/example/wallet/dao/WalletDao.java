package com.example.wallet.dao;

import com.example.wallet.entity.Wallet;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface WalletDao extends CrudRepository<Wallet, Integer> {

}
