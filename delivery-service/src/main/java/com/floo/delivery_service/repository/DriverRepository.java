package com.floo.delivery_service.repository;
import com.floo.delivery_service.entity.Driver;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface DriverRepository extends MongoRepository<Driver, String> {
    List<Driver> findByOnlineTrueAndAvailableTrue();
}
