package com.marllonmendez.literature.modal;

import com.marllonmendez.literature.requestDTO.AuthorRequestDTO;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "authors")
public class AuthorModal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String author;

    private Year birthYear;

    private Year deathYear;

    @OneToMany(mappedBy = "author", fetch = FetchType.EAGER)
    private List<BookModal> livros = new ArrayList<>();

    public static boolean hasYear(Year year) {
        return year != null && !year.equals(Year.of(0));
    }

    public AuthorModal(AuthorRequestDTO data) {
        this.author = data.author();
        this.birthYear = data.birthYear() != null ? Year.of(data.birthYear()) : null;
        this.deathYear = data.deathYear() != null ? Year.of(data.deathYear()) : null;
    }

    public AuthorModal(String author, Year birthYear, Year deathYear) {
        this.author = author;
        this.birthYear = birthYear;
        this.deathYear = deathYear;
    }

    @Override
    public String toString() {
        String birthYearStr = birthYear != null ? birthYear.toString() : "Desconhecido";
        String deathYearStr = deathYear != null ? deathYear.toString() : "Desconhecido";

        return "Autor: " + author + " (nascido em " + birthYearStr + ", falecido em " + deathYearStr + ")";
    }
}
