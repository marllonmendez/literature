package com.marllonmendez.literature.repository;

import com.marllonmendez.literature.modal.AuthorModal;
import com.marllonmendez.literature.modal.BookModal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Year;
import java.util.List;

public interface IBookRepository extends JpaRepository<BookModal, Long> {

    @Query("SELECT book FROM BookModal book WHERE LOWER(book.title) = LOWER(:title)")
    List<BookModal> findByTitle(String title);

    @Query("SELECT author FROM AuthorModal author WHERE author.birthYear <= :year AND (author.deathYear IS NULL OR author.deathYear >= :year)")
    List<AuthorModal> findAuthors(Year year);

    @Query("SELECT language FROM BookModal language WHERE language.language LIKE %:language%")
    List<BookModal> findByLanguage(String language);
}
