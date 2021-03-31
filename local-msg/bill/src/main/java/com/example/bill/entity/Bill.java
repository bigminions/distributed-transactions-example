package com.example.bill.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;

@Data
public class Bill {

    @Id Integer id;

    // 用户
    @Column String username;

    // 级别
    @Column("user_level") Integer userLevel;

    // 更新版本号
    @Version Integer version;

}
