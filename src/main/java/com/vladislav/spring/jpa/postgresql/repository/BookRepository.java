package com.vladislav.spring.jpa.postgresql.repository;

import com.vladislav.spring.jpa.postgresql.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}
