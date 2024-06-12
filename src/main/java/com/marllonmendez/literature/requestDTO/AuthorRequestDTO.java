package com.marllonmendez.literature.requestDTO;

import com.fasterxml.jackson.annotation.JsonAlias;

public record AuthorRequestDTO(
        @JsonAlias("name") String author,
        @JsonAlias("birth_year") Integer birthYear,
        @JsonAlias("death_year") Integer deathYear) {

    @Override
    public String toString() {
        return "Autor: " + author;
    }
}
