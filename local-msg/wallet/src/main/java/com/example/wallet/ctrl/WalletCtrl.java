package com.example.wallet.ctrl;

import com.example.wallet.entity.Wallet;
import com.example.wallet.service.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallet")
@Slf4j
public class WalletCtrl {

    @Autowired
    WalletService walletService;

    /**
     * 查看我的钱包
     *
     * @return
     */
    @GetMapping(path = "/own")
    ResponseEntity<Wallet> own() {
        // 写死用户1
        Integer userId = 1;
        return ResponseEntity.ok(walletService.getOrInitWallet(userId));
    }

    /**
     * 赠送活动 点开即送100
     *
     * @return
     */
    @GetMapping(path = "/giveAct")
    ResponseEntity<String> giveAct() {
        // 写死用户1， 送100金币
        Wallet own = own().getBody();
        boolean flag = walletService.incrWalletTotal(own, 100);

        return ResponseEntity.ok(flag ? "give success $100." : "Please wait a moment");
    }

}