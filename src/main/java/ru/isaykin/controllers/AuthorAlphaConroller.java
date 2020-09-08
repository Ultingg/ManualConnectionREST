package ru.isaykin.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.isaykin.exceptions.NotFoundException;
import ru.isaykin.reader.Author;
import ru.isaykin.reader.DataBaseRepository;
import ru.isaykin.services.AuthorsRepositorySQL;

import java.util.Set;

@RestController
@RequestMapping
public class AuthorAlphaConroller {

    private final DataBaseRepository dataBaseRepository;

    @Autowired
    public AuthorAlphaConroller(DataBaseRepository dataBaseRepository) {
        this.dataBaseRepository = dataBaseRepository;
    }

    @GetMapping("authors")
    public Set<Author> getList() {
        Set<Author> authors = dataBaseRepository.getAllAuthors();

        return authors;
    }

    @GetMapping("authors/{id}")
    public Author getOneAuthor(@PathVariable int id) {
        Set<Author> authors = dataBaseRepository.getAllAuthors();
        return authors.stream()
                .filter(author -> author.getId() == id)
                .findFirst().orElseThrow(NotFoundException::new);
    }

    @GetMapping("authors/")
    public Author getOneAuthorByNameOrLastname(@RequestParam("first_name") String name,
                                               @RequestParam("last_name") String lastName) {
        Set<Author> authors = dataBaseRepository.getAllAuthors();
        return authors.stream().filter(author -> author.getFirstName().equals(name) || author.getLastName().equals(lastName))
                .findFirst()
                .orElseThrow(NotFoundException::new);
    }

    @GetMapping("authors/age/gt/{age}")
    public Set<Author> getListByAge(@PathVariable int age) {
        Set<Author> authors = dataBaseRepository.getAuthorsWithAge(age);
        return authors;
    }

    @PostMapping("authors/insert")
    public String insertAuthorToTable(@RequestParam("first_name") String firstName,
                                      @RequestParam("last_name") String lastName,
                                      @RequestParam("email") String email,
                                      @RequestParam("birthdate") String birthdate) {
        String insertRequest = "INSERT authors (first_name, last_name, email, birthdate) VALUES (\""
                .concat(firstName)
                .concat("\" ,\"")
                .concat(lastName)
                .concat("\" ,\"")
                .concat(email)
                .concat("\" ,\"")
                .concat(birthdate)
                .concat("\")");

        AuthorsRepositorySQL.requestToTable(insertRequest);

        return "added";
    }

    @DeleteMapping("authors/{id}")
    public String delete(@PathVariable String id) {
        String deleteRequest = "DELETE FROM authors WHERE id = " + id;

        AuthorsRepositorySQL.requestToTable(deleteRequest);

        return "Author id: "
                .concat(id)
                .concat(" was deleted");
    }

    @PutMapping("authors/update/{id}")
    public String updateById(@PathVariable int id,
                             @RequestParam("key_parameter") String keyParameter,
                             @RequestParam("value_parameter") String valueParameter) {
        String updateRequestString = "UPDATE authors SET "
                .concat(keyParameter)
                .concat(" = \"")
                .concat(valueParameter)
                .concat("\" WHERE id = \"")
                .concat(String.valueOf(id))
                .concat("\"");

        AuthorsRepositorySQL.requestToTable(updateRequestString);

        return "Author id: "
                .concat(String.valueOf(id))
                .concat(" was updated");

    }

}
