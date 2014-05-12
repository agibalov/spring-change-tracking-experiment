package me.loki2302.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.loki2302.dto.ChangeLogTransactionDto;
import me.loki2302.changelog.ChangeLogEvent;
import me.loki2302.entities.ChangeLogTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
class TransactionMapper {
    @Autowired
    private ObjectMapper objectMapper;

    public List<ChangeLogTransactionDto> makeChangeLogTransactionDtos(List<ChangeLogTransaction> changeLogTransactions) {
        List<ChangeLogTransactionDto> changeLogTransactionDtos = new ArrayList<ChangeLogTransactionDto>();
        for(ChangeLogTransaction changeLogTransaction : changeLogTransactions) {
            ChangeLogTransactionDto changeLogTransactionDto = makeChangeLogTransactionDto(changeLogTransaction);
            changeLogTransactionDtos.add(changeLogTransactionDto);
        }
        return changeLogTransactionDtos;
    }

    public ChangeLogTransactionDto makeChangeLogTransactionDto(ChangeLogTransaction changeLogTransaction) {
        ChangeLogTransactionDto changeLogTransactionDto = new ChangeLogTransactionDto();
        changeLogTransactionDto.id = changeLogTransaction.id;
        try {
            changeLogTransactionDto.events = objectMapper.readValue(
                    changeLogTransaction.changeLogEventsJson,
                    new TypeReference<List<ChangeLogEvent>>() {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return changeLogTransactionDto;
    }
}
