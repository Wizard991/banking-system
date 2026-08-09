package com.banking.accountservice.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class AccountEventConsumer {

    private final AccountService accountService;

    /*
       * Consume transaction.completed event from kafta
       * Credits receiver account
       * @param Payload
     */

    @KafkaListener(topics = "transaction.completed")
    public void consumeTransactionCompleted(@Payload Map<String, Object> payload){

        try{
            String receiverAccount = (String) payload.get("receiverAccountNumber");
            BigDecimal amount = new BigDecimal(payload.get("amount").toString());

            log.info("Crediting account: {} amount: {}", receiverAccount, amount);
            accountService.creditBalance(receiverAccount, amount);

        }catch (Exception e){
            log.info("Error crediting account: {}",e.getMessage());
        }
    }


    /*
        * Consume fraud.detected event from kafta
        * Blocks the flagged account.
        * @param payload
     */

    @KafkaListener(topics = "fraud.detected")
    public void consumeFraudDetected(@Payload Map<String, Object> payload){

        try{
            String accountNumber = (String) payload.get("accountNumber");
            log.info("Fraud Detected - Blocking account: {}",accountNumber);

            accountService.blockAccount(accountNumber);
        }catch (Exception e){
            log.info("Error blocking account: {}",e.getMessage());
        }
    }
}
