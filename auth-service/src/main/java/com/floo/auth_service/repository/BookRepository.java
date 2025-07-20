package com.floo.auth_service.repository;

import com.floo.auth_service.model.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

public interface BookRepository extends MongoRepository<Book,String> {
}
