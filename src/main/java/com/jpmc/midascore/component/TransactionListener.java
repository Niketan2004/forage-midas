package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.Incentive;
import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TransactionListener {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private RestTemplate restTemplate;


    // TASK 4
    @Transactional
    @KafkaListener(topics = "${general.kafka-topic}")
    public void listen(Transaction transaction) {
        UserRecord sender = userRepository.findById(transaction.getSenderId());
        UserRecord recipient = userRepository.findById(transaction.getRecipientId());

        if (isValid(sender, recipient, transaction.getAmount())) {
            // 1. Call the Incentive API
            String incentiveUrl = "http://localhost:8080/incentive";
            Incentive incentiveResponse = restTemplate.postForObject(incentiveUrl, transaction, Incentive.class);
            float incentiveAmount = (incentiveResponse != null) ? incentiveResponse.getAmount() : 0.0f;

            // 2. Update balances
            // Sender pays the amount
            sender.setBalance(sender.getBalance() - transaction.getAmount());
            // Recipient gets amount + incentive
            recipient.setBalance(recipient.getBalance() + transaction.getAmount() + incentiveAmount);

            // 3. Save updates
            userRepository.save(sender);
            userRepository.save(recipient);

            // 4. Record the transaction with the incentive
            TransactionRecord record = new TransactionRecord();
            record.setSender(sender);
            record.setReciever(recipient);
            record.setAmount(transaction.getAmount());
            record.setIncentive(incentiveAmount);
            transactionRepository.save(record);
        }
    }
// TASK 3
//    @Transactional
//    @KafkaListener(topics = "${general.kafka-topic}")
//    public void listen(Transaction transaction) {
//        UserRecord sender = userRepository.findById(transaction.getSenderId());
//        UserRecord recipient = userRepository.findById(transaction.getRecipientId());
//
//        if (isValid(sender, recipient, transaction.getAmount())) {
//            // Update balances
//            sender.setBalance(sender.getBalance() - transaction.getAmount());
//            recipient.setBalance(recipient.getBalance() + transaction.getAmount());
//
//            // Save updated users
//            userRepository.save(sender);
//            userRepository.save(recipient);
//
//            // Record the transaction
//            TransactionRecord record = new TransactionRecord(sender, recipient, transaction.getAmount());
//            transactionRepository.save(record);
//        }
//    }
//
    private boolean isValid(UserRecord sender, UserRecord recipient, float amount) {
        return sender != null && recipient != null && sender.getBalance() >= amount;
    }
}