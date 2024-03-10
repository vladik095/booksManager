package com.vladislav.spring.jpa.postgresql.repository;

import com.vladislav.spring.jpa.postgresql.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
}
