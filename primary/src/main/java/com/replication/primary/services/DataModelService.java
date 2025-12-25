package com.replication.primary.services;

import com.replication.primary.dtos.DataModelRequest;
import com.replication.primary.dtos.DataModelUpdateRequest;
import com.replication.primary.exceptions.DataModelNotFoundException;
import com.replication.primary.models.DataModel;
import com.replication.primary.repositories.DataModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DataModelService {
    private final DataModelRepository dataModelRepository;
    private final WebClient webClient;

    @Value("${replica.uris}")
    public List<String> replicationUris;

    @Transactional
    public DataModel registerDataModel(DataModelRequest dataModelRequest) {
        DataModel dataModel = new DataModel();
        dataModel.setName(dataModelRequest.getName());
        dataModel.setBalance(dataModelRequest.getBalance());
        dataModel.setCreatedAt(LocalDateTime.now());
        dataModel = dataModelRepository.save(dataModel);
        DataModel finalDataModel = dataModel;
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        for(String uri : replicationUris) {
                            webClient.post()
                                    .uri(uri)
                                    .bodyValue(finalDataModel)
                                    .retrieve()
                                    .toBodilessEntity()
                                    .subscribe();
                        }
                    }
                }
        );
        return dataModel;
    }

    @Transactional
    public List<DataModel> getAllDataModels() {
        return dataModelRepository.findAll();
    }

    @Transactional
    public DataModel getDataModelById(UUID id) {
        return dataModelRepository.findById(id).orElseThrow(() -> new DataModelNotFoundException("Data model not found"));
    }

    @Transactional
    public DataModel updateDataModel(DataModelUpdateRequest dataModelUpdateRequest) {
        DataModel dataModel = dataModelRepository.findById(dataModelUpdateRequest.getId()).orElseThrow(() -> new DataModelNotFoundException("Data model not found"));
        dataModel.setBalance(dataModelUpdateRequest.getBalance());
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        for(String uri : replicationUris) {
                            webClient.put()
                                    .uri(uri)
                                    .bodyValue(dataModelUpdateRequest)
                                    .retrieve()
                                    .toBodilessEntity()
                                    .subscribe();
                        }
                    }
                }
        );
        return dataModel;
    }

    public void deleteDataModel(UUID id) {
        dataModelRepository.deleteById(id);
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        for(String uri : replicationUris) {
                            webClient.delete()
                                    .uri(uri + id)
                                    .retrieve()
                                    .toBodilessEntity()
                                    .subscribe();
                        }
                    }
                }
        );
    }
}
