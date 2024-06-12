package com.marllonmendez.literature.requestDTO;

import com.fasterxml.jackson.annotation.JsonAlias;
import java.util.List;

public record BookRequestDTO(
        @JsonAlias("title") String title,
        @JsonAlias("download_count") Double downloadCount,
        @JsonAlias("languages") List<String> language,
        @JsonAlias("authors") List<AuthorRequestDTO> authors) {

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Título: ").append(title).append("\n");
        builder.append("Autor: \n");
        for (AuthorRequestDTO author : authors) {
            builder.append(" - ").append(author.author()).append("\n");
        }
        builder.append("Idiomas: ").append(String.join(", ", language)).append("\n");
        builder.append("Downloads: ").append(downloadCount).append("\n");
        return builder.toString();
    }
}
