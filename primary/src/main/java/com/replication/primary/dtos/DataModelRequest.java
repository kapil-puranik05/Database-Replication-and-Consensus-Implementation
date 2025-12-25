package com.replication.primary.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DataModelRequest {
    private String name;
    private BigDecimal balance;
}
