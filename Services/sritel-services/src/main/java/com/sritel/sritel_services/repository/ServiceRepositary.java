package com.sritel.sritel_services.repository;

import com.sritel.sritel_services.entity.Service;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ServiceRepositary extends MongoRepository<Service,String> {
}
