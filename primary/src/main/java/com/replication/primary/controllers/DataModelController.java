package com.replication.primary.controllers;

import com.replication.primary.dtos.DataModelRequest;
import com.replication.primary.dtos.DataModelUpdateRequest;
import com.replication.primary.services.DataModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/primary")
@RequiredArgsConstructor
public class DataModelController {
    private final DataModelService dataModelService;

    @PostMapping
    public ResponseEntity<?> register(DataModelRequest dataModelRequest) {
        return new ResponseEntity<>(dataModelService.registerDataModel(dataModelRequest), HttpStatus.CREATED);
    }

    @PatchMapping
    public ResponseEntity<?> update(DataModelUpdateRequest dataModelUpdateRequest) {
        return new ResponseEntity<>(dataModelService.updateDataModel(dataModelUpdateRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        dataModelService.deleteDataModel(id);
        return new ResponseEntity<>("Data Model deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable UUID id) {
        return new ResponseEntity<>(dataModelService.getDataModelById(id), HttpStatus.OK);
    }
}
