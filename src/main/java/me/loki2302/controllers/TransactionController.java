package me.loki2302.controllers;

import me.loki2302.entities.ChangeLogTransaction;
import me.loki2302.entities.ChangeLogTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private ChangeLogTransactionRepository changeLogTransactionRepository;

    @RequestMapping(value = "/first")
    public Object getFirstTransaction() {
        Page<ChangeLogTransaction> changeLogTransactions =
                changeLogTransactionRepository.findAll(new PageRequest(0, 1, Sort.Direction.ASC, "id"));
        if(!changeLogTransactions.hasContent()) {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }

        ChangeLogTransaction first = changeLogTransactions.getContent().get(0);
        return new ResponseEntity<ChangeLogTransaction>(first, HttpStatus.OK);
    }

    @RequestMapping(value = "/last")
    public Object getLastTransaction() {
        Page<ChangeLogTransaction> changeLogTransactions =
                changeLogTransactionRepository.findAll(new PageRequest(0, 1, Sort.Direction.DESC, "id"));
        if(!changeLogTransactions.hasContent()) {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }

        ChangeLogTransaction first = changeLogTransactions.getContent().get(0);
        return new ResponseEntity<ChangeLogTransaction>(first, HttpStatus.OK);
    }

    @RequestMapping(value = "/after/{firstId}")
    public Object getTransactionsAfter(@PathVariable long firstId) {
        List<ChangeLogTransaction> transactions = changeLogTransactionRepository.findByIdGreaterThan(firstId);
        return transactions;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ChangeLogTransaction> getAllTransactions() {
        return changeLogTransactionRepository.findAll();
    }
}
