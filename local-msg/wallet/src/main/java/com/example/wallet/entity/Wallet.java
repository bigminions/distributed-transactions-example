package com.example.wallet.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;

import java.math.BigDecimal;

@Data
public class Wallet {

    @Id Integer id;

    // 用户
    @Column("user_id") Integer userId;

    // 级别
    @Column BigDecimal total;

    // 更新版本号
    @Version Integer version;

}
