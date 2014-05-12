package me.loki2302.controllers;

import me.loki2302.dto.ChangeLogTransactionDto;
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

    @Autowired
    private TransactionMapper transactionMapper;

    @RequestMapping(value = "/first")
    public Object getFirstTransaction() {
        Page<ChangeLogTransaction> changeLogTransactions =
                changeLogTransactionRepository.findAll(new PageRequest(0, 1, Sort.Direction.ASC, "id"));
        if(!changeLogTransactions.hasContent()) {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }

        ChangeLogTransaction first = changeLogTransactions.getContent().get(0);
        ChangeLogTransactionDto dto = transactionMapper.makeChangeLogTransactionDto(first);
        return new ResponseEntity<ChangeLogTransactionDto>(dto, HttpStatus.OK);
    }

    @RequestMapping(value = "/last")
    public Object getLastTransaction() {
        Page<ChangeLogTransaction> changeLogTransactions =
                changeLogTransactionRepository.findAll(new PageRequest(0, 1, Sort.Direction.DESC, "id"));
        if(!changeLogTransactions.hasContent()) {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }

        ChangeLogTransaction last = changeLogTransactions.getContent().get(0);
        ChangeLogTransactionDto dto = transactionMapper.makeChangeLogTransactionDto(last);
        return new ResponseEntity<ChangeLogTransactionDto>(dto, HttpStatus.OK);
    }

    @RequestMapping(value = "/after/{firstId}")
    public Object getTransactionsAfter(@PathVariable long firstId) {
        List<ChangeLogTransaction> transactions = changeLogTransactionRepository.findByIdGreaterThan(firstId);
        List<ChangeLogTransactionDto> dtos = transactionMapper.makeChangeLogTransactionDtos(transactions);
        return dtos;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ChangeLogTransactionDto> getAllTransactions() {
        List<ChangeLogTransaction> transactions = changeLogTransactionRepository.findAll();
        List<ChangeLogTransactionDto> dtos = transactionMapper.makeChangeLogTransactionDtos(transactions);
        return dtos;
    }
}
