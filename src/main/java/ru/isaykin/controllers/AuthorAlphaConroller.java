package ru.isaykin.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.isaykin.reader.Author;
import ru.isaykin.services.AuthorsServicSQL;

import java.util.Set;
import java.util.TreeSet;

@RestController
@RequestMapping
public class AuthorAlphaConroller {

    private final AuthorsServicSQL authorsServicSQL;

    @Autowired
    public AuthorAlphaConroller(AuthorsServicSQL authorsServicSQL) {

        this.authorsServicSQL = authorsServicSQL;
    }

    @GetMapping("authors")
    public Set<Author> getList() {
        Set<Author> authors = new TreeSet<>(authorsServicSQL.getAll()); //TODO: заменить все Set на List

        return authors;
    }

    @GetMapping("authors/{id}")
    public Author getOneAuthor(@PathVariable int id) {
        return authorsServicSQL.getOneById(id);

    }

    @GetMapping("authors/")
    public Author getOneAuthorByNameOrLastname(@RequestParam("first_name") String firstname,
                                               @RequestParam("last_name") String lastName) {
        return authorsServicSQL.getByFirstNameAndLastName(firstname, lastName);
    }

    @GetMapping("authors/age/gt/{age}")
    public Set<Author> getListByAge(@PathVariable int age) {
        return authorsServicSQL.getListByAge(age);
    }

    @PostMapping("authors/insert")
    public String insertAuthorToTable(@RequestParam("first_name") String firstName,
                                      @RequestParam("last_name") String lastName,
                                      @RequestParam("email") String email,
                                      @RequestParam("birthdate") String birthdate) {
        authorsServicSQL.insertAuthorToTable(firstName, lastName, email, birthdate);
        return "added";
    }

    @DeleteMapping("authors/{id}")
    public String delete(@PathVariable int id) {
        authorsServicSQL.delete(id);

        return "Author id: "
                .concat(String.valueOf(id))
                .concat(" was deleted");
    }

    @PutMapping("authors/update/{id}")
    public String updateById(@PathVariable int id,
                             @RequestParam("key_parameter") String keyParameter,
                             @RequestParam("value_parameter") String valueParameter) {
        authorsServicSQL.update(id, keyParameter, valueParameter);

        return "Author id: "
                .concat(String.valueOf(id))
                .concat(" was updated");

    }

}
