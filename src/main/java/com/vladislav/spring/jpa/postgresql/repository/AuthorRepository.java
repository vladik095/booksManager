package com.vladislav.spring.jpa.postgresql.repository;

import com.vladislav.spring.jpa.postgresql.model.Author;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Author findByName(String name);

    @Query(value = "SELECT * FROM Author WHERE LOWER(name) LIKE LOWER(CONCAT('%', :keyword, '%'))", nativeQuery = true)
    List<Author> findByNameContaining(@Param("keyword") String keyword);

}
