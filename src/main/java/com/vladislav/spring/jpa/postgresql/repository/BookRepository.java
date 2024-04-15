package com.vladislav.spring.jpa.postgresql.repository;

import com.vladislav.spring.jpa.postgresql.model.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query(value = "SELECT * FROM Book WHERE LOWER(title) LIKE LOWER(CONCAT('%', :keyword, '%'))", nativeQuery = true)
    List<Book> findByTitleContaining(@Param("keyword") String keyword);
}
