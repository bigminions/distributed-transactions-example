package com.example.wallet.ctrl;

import com.example.wallet.dao.WalletDao;
import com.example.wallet.entity.Wallet;
import com.example.wallet.service.WalletService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping(path = "/act")
@Slf4j
public class WalleLocalMsgExampleCtrl {

    @Autowired
    WalletService walletService;

    /**
     * 从某个支付系统回调回来的钱包支付完成的 业务请求， 我们将在这里完成分布式事物 -- 本地消息表
     * @param price 金额 支付金额
     * @param uid 用户id 用户id
     * @return
     */
    @PostMapping(path = "/callback")
    ResponseEntity<String> actCallback(@RequestParam Integer price, @RequestParam Integer uid) {
        try {
            walletService.actCallback(uid, price);
        } catch (JsonProcessingException e) {
            return ResponseEntity.ok("网络繁忙");
        }
        return ResponseEntity.ok("success");
    }


}
