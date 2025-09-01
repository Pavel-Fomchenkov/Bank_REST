package com.example.bankcards.util;

import com.example.bankcards.dto.TransactionDTO;
import com.example.bankcards.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TransactionMapper {
    @Named("mapToTransactionDTO")
    @Mapping(target = "amount", expression = "java(transaction.getAmount().toString())")
    TransactionDTO mapToTransactionDTO(Transaction transaction);
}
