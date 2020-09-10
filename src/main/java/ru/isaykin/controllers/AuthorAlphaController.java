package ru.isaykin.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.isaykin.exceptions.NotFoundException;
import ru.isaykin.reader.Author;
import ru.isaykin.reader.DataBaseRepository;
import ru.isaykin.services.AuthorsRepositorySQL;

import java.util.List;

@RestController
@RequestMapping
public class AuthorAlphaController {

    private final AuthorsServicSQL authorsServicSQL;

    @Autowired
    public AuthorAlphaConroller(DataBaseRepository dataBaseRepository) {
        this.dataBaseRepository = dataBaseRepository;
    }


    @GetMapping("authors")
    public Set<Author> getList() {
        Set<Author> authors = DataBaseRepository.getAllAuthors();

        if (firstName != null && lastName != null) {
            return authorService.getByFirstNameAndLastName(firstName,lastName);
        } else {
            return authorService.getAll();
        }
    }

    @GetMapping("authors/{id}")
    public Author getOneAuthor(@PathVariable int id) {
        List<Author> authors = dataBaseRepository.getAllAuthors();
        return authors.stream()
                .filter(author -> author.getId() == id)
                .findFirst().orElseThrow(NotFoundException::new);
        return authorsServicSQL.getOneById(id);

    }

    @GetMapping("authors/")
    public Author getOneAuthorByNameOrLastname(@RequestParam("first_name") String firstname,
                                               @RequestParam("last_name") String lastName) {
        return authorsServicSQL.getByFirstNameAndLastName(firstname, lastName);
    }

    @GetMapping("authors/age/gt/{age}")
    public List<Author> getListByAge(@PathVariable int age) {
        return authorsServicSQL.getListByAge(age);
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
