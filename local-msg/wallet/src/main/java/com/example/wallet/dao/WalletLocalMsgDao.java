package com.example.wallet.dao;

import com.example.wallet.entity.Wallet;
import com.example.wallet.entity.WalletLocalMsg;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WalletLocalMsgDao extends CrudRepository<WalletLocalMsg, Integer> {

    @Query("select * from wallet_local_msg where is_sent = 0 and id > :minId order by id asc limit :limitCnt")
    List<WalletLocalMsg> findUnSentBatch(@Param("minId") Integer minId, @Param("limitCnt") Integer limitCnt);
}
