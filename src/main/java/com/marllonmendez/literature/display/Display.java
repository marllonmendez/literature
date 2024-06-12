package com.marllonmendez.literature.display;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.marllonmendez.literature.modal.AuthorModal;
import com.marllonmendez.literature.modal.BookModal;
import com.marllonmendez.literature.repository.IBookRepository;
import com.marllonmendez.literature.requestDTO.BookRequestDTO;
import com.marllonmendez.literature.service.ConvertData;
import com.marllonmendez.literature.service.ServiceAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Year;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

@Component
public class Display {

    @Autowired
    private IBookRepository iBookRepository;

    @Autowired
    private ServiceAPI serviceAPI;

    @Autowired
    private ConvertData convertData;

    private final Scanner scanner = new Scanner(System.in);

    public Display(IBookRepository IBookRepository, ServiceAPI serviceAPI, ConvertData convertData) {
        this.iBookRepository = IBookRepository;
        this.serviceAPI = serviceAPI;
        this.convertData = convertData;
    }

    public void options() {
        boolean run = true;
        while (run) {
            menu();
            var option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> searchBooksFromTitle();
                case 2 -> bookList();
                case 3 -> authorList();
                case 4 -> livingAuthorsList();
                case 5 -> booksLanguageList();
                case 0 -> {
                    System.out.println("Encerrando a LiterAlura!");
                    run = false;
                }
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private void menu() {
        System.out.println("""
        ----------------------------------------------------------------------
        -                         ESCOLHA UMA OPÇÃO:                         -
        ----------------------------------------------------------------------
        -              1- Buscar livros pelo título                          -
        -              2- Listar livros registrados                          -
        -              3- Listar autores registrados                         -
        -              4- Listar autores vivos em um determinado ano         -
        -              5- Listar livros em um determinado idioma             -
        -              0- Sair                                               -
        ----------------------------------------------------------------------
        """);
    }

    private void saveBooks(List<BookModal> bookModalList) {
        iBookRepository.saveAll(bookModalList);
    }

    private void searchBooksFromTitle() {
        String baseURL = "https://gutendex.com/books?search=";

        try {
            System.out.println("Digite o nome do livro: ");
            String title = scanner.nextLine();
            String address = baseURL + title.replace(" ", "%20");
            System.out.println("URL da API: " + address);

            String jsonRes = serviceAPI.getData(address);
            System.out.println("Resposta da API: " + jsonRes);

            if (jsonRes.isEmpty()) {
                System.out.println("Resposta Invalida!");
            }

            JsonNode jsonNode = convertData.getMapper().readTree(jsonRes);
            JsonNode results = jsonNode.path("results");

            if (results.isEmpty()) {
                System.out.println("Livro não encontrado!");
            }

            List<BookRequestDTO> data = convertData.getMapper().readerForListOf(BookRequestDTO.class).readValue(results);

            List<BookModal> existingBooks = iBookRepository.findByTitle(title);

            if (!existingBooks.isEmpty()) {
                System.out.println("Removendo livros duplicados...");
                for (BookModal bookModal : existingBooks) {
                    data.removeIf(bookRequestDTO -> bookModal.getTitle().equals(bookRequestDTO.title()));
                }
            }

            if(!data.isEmpty()) {
                System.out.println("Resgistrado Livros...");
                List<BookModal> newBooks = data.stream().map(BookModal::new).toList();
                saveBooks(newBooks);
                System.out.println("Livros registrados com sucesso!");
            } else {
                System.out.println("Todos os Livros já estão registrados.");
            }

            if (!data.isEmpty()) {
                System.out.println("Livros encontrados:");
                Set<String> titles = new HashSet<>();
                for (BookRequestDTO bookRequestDTO : data) {
                    if (!titles.contains(bookRequestDTO.title())) {
                        System.out.println(bookRequestDTO);
                        titles.add(bookRequestDTO.title());
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void bookList() {
        List<BookModal> books = iBookRepository.findAll();
        if (books.isEmpty()) {
            System.out.println("Nenhum livro encontrado!");
        } else {
            books.forEach(System.out::println);
        }
    }

    private void authorList() {
        List<BookModal> authorBook = iBookRepository.findAll();
        if (authorBook.isEmpty()) {
            System.out.println("Nenhum autor encontrado!");
        } else {
            authorBook.stream().map(BookModal::getAuthor).distinct().forEach(author -> System.out.println(author.getAuthor()));
        }
    }

    private void livingAuthorsList() {
        System.out.println("Digite o ano: ");
        Integer authorYear = scanner.nextInt();
        scanner.nextLine();

        Year year = Year.of(authorYear);

        List<AuthorModal> authors = iBookRepository.findAuthors(year);
        if (authors.isEmpty()) {
            System.out.println("Nenhum autor vivo encontrado!");
        } else {
            System.out.println("Lista de autores vivos em " + year + ":\n");

            authors.forEach(authorModal -> {
                if (AuthorModal.hasYear(authorModal.getBirthYear()) && AuthorModal.hasYear(authorModal.getDeathYear())) {
                    String author = authorModal.getAuthor();
                    String birthYear = authorModal.getBirthYear().toString();
                    String deathYear = authorModal.getDeathYear().toString();
                    System.out.println(author + " (" + birthYear + " - " + deathYear + ")");
                }
            });
        }
    }

    private void booksLanguageList() {
        System.out.println("""
        ----------------------------------------------------------------------
        -                         DIGITE UM IDIOMA:                          -
        ----------------------------------------------------------------------
        -                           en - Inglês                              -
        -                           pt - Português                           -
        -                           es - Espanhol                            -
        -                           fr - Francês                             -
        ----------------------------------------------------------------------
        """);
        String language = scanner.nextLine();

        List<BookModal> books = iBookRepository.findByLanguage(language);
        if (books.isEmpty()) {
            System.out.println("Nenhum livro encontrado!");
        } else {
           books.forEach(bookModal -> {
               String title = bookModal.getTitle();
               String author = bookModal.getAuthor().getAuthor();
               String lang = bookModal.getLanguage();

               System.out.println("----------------------------------------");
               System.out.println("Título: " + title);
               System.out.println("Autor: " + author);
               System.out.println("Idioma: " + lang);
               System.out.println("----------------------------------------");
           });
        }
    }
}
