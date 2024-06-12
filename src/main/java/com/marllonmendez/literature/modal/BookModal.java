package com.marllonmendez.literature.modal;

import com.marllonmendez.literature.requestDTO.BookRequestDTO;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "books")
public class BookModal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String title;

    @ManyToOne(cascade = CascadeType.ALL)
    private AuthorModal author;

    private String language;

    private Integer birthYearAuthor;

    private Integer deathYearAuthor;

    private Double downloadCount;

    public BookModal(BookRequestDTO data) {
        this.title = data.title();
        this.author = new AuthorModal(data.authors().getFirst());
        this.language = data.language().getFirst();
        this.downloadCount = data.downloadCount();
    }

    public BookModal(Long idAPI, String title, AuthorModal author, String language, Double downloadCount) {
        this.title = title;
        this.author = author;
        this.language = language;
        this.downloadCount = downloadCount;
    }

    @Override
    public String toString() {
        return "Título: " + title + "\n" +
                "Autor: " + author + "\n" +
                "Idioma: " + language + "\n" +
                "Downloads: " + downloadCount + "\n" +
                "----------------------------------------";
    }
}
