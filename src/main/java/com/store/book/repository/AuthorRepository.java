package com.store.book.repository;

import com.store.book.model.Author;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query("SELECT a FROM Author a WHERE UPPER(a.name) LIKE CONCAT('%', UPPER(:name), '%')")
    List<Author> findAllByNameContainingIgnoreCase(@Param("name") String name);

}
