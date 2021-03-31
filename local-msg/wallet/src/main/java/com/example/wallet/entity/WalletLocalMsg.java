package com.example.wallet.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;

@Data
@AccessType(AccessType.Type.PROPERTY)
@Table("wallet_local_msg")
public class WalletLocalMsg {

    public enum BussType {
        // 活动类型
        ACT_TYPE,
        // ... 其他各种业务类型
    }

    @Id Integer id;

    @Column("buss_type")
    public Integer intBussType;

    @Transient
    BussType bussType;

    @Column String topic;

    @Column String data;

    @Column("retry_cnt") Integer retryCnt;

    @Column("is_sent") Boolean isSent;

    @Column("is_consumed") Boolean isConsumed;

    @Column("last_send") Date lastSend;

    @Column("create_time") Date createTime;

    @Column @Version Integer version;


    public BussType getBussType() {
        if (bussType == null) {
            bussType = Arrays.stream(BussType.values()).filter(v -> v.ordinal() == intBussType).findAny().orElse(null);
        }
        return bussType;
    }

    public void setBussType(BussType bussType) {
        this.bussType = bussType;
        this.intBussType = bussType.ordinal();
    }
}
