package com.example.wallet.service;

import com.example.wallet.cfg.RabbitMqCfg;
import com.example.wallet.dao.WalletDao;
import com.example.wallet.dao.WalletLocalMsgDao;
import com.example.wallet.entity.Wallet;
import com.example.wallet.entity.WalletLocalMsg;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

@Service
@Slf4j
public class WalletService {

    @Autowired
    WalletDao walletDao;

    @Autowired
    WalletLocalMsgDao walletLocalMsgDao;

    @Autowired
    WalletMsgProducer walletMsgProducer;

    @Transactional
    public void actCallback(Integer uid, Integer price) throws JsonProcessingException {
        Wallet wallet = getOrInitWallet(uid);
        incrWalletTotal(wallet, -price);

        WalletLocalMsg walletLocalMsg = new WalletLocalMsg();
        walletLocalMsg.setId(null);
        walletLocalMsg.setBussType(WalletLocalMsg.BussType.ACT_TYPE);
        walletLocalMsg.setTopic("ACT_TOPIC");

        ObjectMapper om = new ObjectMapper();
        String data = om.writeValueAsString(om.createObjectNode().put("userId", uid));

        walletLocalMsg.setData(data);
        walletLocalMsg.setRetryCnt(0);
        walletLocalMsg.setIsConsumed(false);
        walletLocalMsg.setIsSent(false);
        walletLocalMsg.setLastSend(new Date());
        walletLocalMsg.setCreateTime(new Date());

        walletLocalMsgDao.save(walletLocalMsg);
        log.debug("save wallet local msg success : {}", walletLocalMsg.getId());

        walletMsgProducer.sendToRabbitMQAsync(RabbitMqCfg.QueueName.ACT_QUEUE, walletLocalMsg);
    }

    public boolean incrWalletTotal (Wallet own, Integer incr) {
        return incrWalletTotal(own, new BigDecimal(incr));
    }

    boolean incrWalletTotal (Wallet own, BigDecimal incr) {
        own.setTotal(own.getTotal().add(incr));
        if (own.getTotal().compareTo(BigDecimal.ZERO) < 0)
            throw new RuntimeException("账户余额不足");
        try {
            walletDao.save(own);
        } catch (Exception e) {
            // OptimisticLockingFailureException 悲观锁：钱包有人正在操作
            log.error("something fail " + e.getMessage(), e);
            return false;
        }
        return true;
    }

    public Wallet getOrInitWallet(Integer userId) {
        // 初始送100金币， 初始化未做并发控制，不过数据库限制了id唯一
        return walletDao.findById(userId).orElseGet(() -> {
            Wallet newWallet = new Wallet();
            newWallet.setId(userId);
            newWallet.setUserId(userId);
            newWallet.setTotal(new BigDecimal("100"));
            return walletDao.save(newWallet);
        });
    }
}
