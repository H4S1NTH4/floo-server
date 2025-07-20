package com.floo.auth_service.service;

import com.floo.auth_service.model.Book;
import com.floo.auth_service.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks(){
       return bookRepository.findAll();
    }

    public Optional<Book> findBookById(String id){
        return bookRepository.findById(id);
    }

    public Book createBook(Book book){
       return  bookRepository.save(book);
    }
}
