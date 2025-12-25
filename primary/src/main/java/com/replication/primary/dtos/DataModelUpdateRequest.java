package com.replication.primary.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class DataModelUpdateRequest {
    private UUID id;
    private BigDecimal balance;
}
