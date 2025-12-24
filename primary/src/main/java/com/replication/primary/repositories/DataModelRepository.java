package com.replication.primary.repositories;

import com.replication.primary.models.DataModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DataModelRepository extends JpaRepository<DataModel, UUID> {
}
