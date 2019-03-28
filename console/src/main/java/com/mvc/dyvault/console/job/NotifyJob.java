package com.mvc.dyvault.console.job;

import com.mvc.dyvault.common.bean.BusinessTransaction;
import com.mvc.dyvault.console.service.BusinessTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author qiyichen
 * @create 2019/3/28 17:26
 */
@Component
public class NotifyJob implements CommandLineRunner {

    @Autowired
    BusinessTransactionService transactionService;

    @Async
    @Override
    public void run(String... args) throws Exception {
        while (true) {
            try {
                Thread.sleep(10);
                BusinessTransaction tx = transactionService.getOverTx();
                transactionService.cancel(tx.getUserId(), tx.getId());
            } catch (Exception e) {
                continue;
            }
        }
    }
}
