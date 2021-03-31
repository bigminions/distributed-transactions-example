package com.example.wallet.schedule;

import com.example.wallet.dao.WalletLocalMsgDao;
import com.example.wallet.entity.WalletLocalMsg;
import com.example.wallet.service.WalletMsgProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class LocalMsgScheduler {

    @Autowired
    private WalletLocalMsgDao walletLocalMsgDao;

    @Autowired
    private WalletMsgProducer walletMsgProducer;

    @Scheduled(cron = "0/30 * * * * ?")
    public void scanUnSentMsgAndRetry() {
        Integer minId = 0;
        while (true) {
            List<WalletLocalMsg> unsentList = walletLocalMsgDao.findUnSentBatch(minId, 10) ;
            if (unsentList == null || unsentList.isEmpty())
                break;

            unsentList.stream().collect(Collectors.partitioningBy())
            Collectors.partitioningBy(msg -> ((WalletLocalMsg) msg).getRetryCnt() > 5, )
            minId = unsentList.get(unsentList.size() - 1).getId();
            unsentList.forEach(msg -> {
                msg.setRetryCnt(msg.getRetryCnt() + 1);
                msg.setLastSend(new Date());
                walletLocalMsgDao.save(msg);

                walletMsgProducer.sendToRabbitMQAsync(msg.getTopic(), msg);
            });
        }
    }
}
