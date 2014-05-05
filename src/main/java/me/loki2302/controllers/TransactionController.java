package me.loki2302.controllers;

import me.loki2302.entities.ChangeLogTransaction;
import me.loki2302.entities.ChangeLogTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transactions/")
public class TransactionController {
    @Autowired
    private ChangeLogTransactionRepository changeLogTransactionRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ChangeLogTransaction> getAllTransactions() {
        return changeLogTransactionRepository.findAll();
    }
}
