package ru.isaykin.controllers;


import org.springframework.web.bind.annotation.*;
import ru.isaykin.reader.Author;
import ru.isaykin.reader.DataBaseRepository;
import ru.isaykin.services.AuthorsRepositorySQL;
import ru.isaykin.services.AuthorsSQLService;

import java.util.List;

@RestController
@RequestMapping
public class AuthorAlphaController {

    private final AuthorsSQLService authorsSQLService;
    private final DataBaseRepository dataBaseRepository;
    private final AuthorsRepositorySQL authorsRepositorySQL;

    public AuthorAlphaController(AuthorsSQLService authorsSQLService, DataBaseRepository dataBaseRepository, AuthorsRepositorySQL authorsRepositorySQL) {
        this.authorsSQLService = authorsSQLService;
        this.dataBaseRepository = dataBaseRepository;
        this.authorsRepositorySQL = authorsRepositorySQL;
    }


    @GetMapping("authors")
    public Object getListOrGetOneByFirstNameAndLastName(@RequestParam("first_name") String firstName,
                                                        @RequestParam("last_name") String lastName) {
        List<Author> authors = dataBaseRepository.getAllAuthors();

        if (firstName != null && lastName != null) {
            return authorsSQLService.getByFirstNameAndLastName(firstName, lastName);
        } else {
            return authorsSQLService.getAll();
        }
    }

    @GetMapping("authors/{id}")
    public Author getOneAuthor(@PathVariable int id) {
        List<Author> authors = dataBaseRepository.getAllAuthors();
        return authorsSQLService.getOneById(id);

    }


    @GetMapping("authors/age/gt/{age}")
    public List<Author> getListByAge(@PathVariable int age) {
        return authorsSQLService.getListByAge(age);
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

        authorsRepositorySQL.requestToTable(insertRequest);

        return "added";
    }

    @DeleteMapping("authors/{id}")
    public String delete(@PathVariable String id) {
        String deleteRequest = "DELETE FROM authors WHERE id = " + id;

        authorsRepositorySQL.requestToTable(deleteRequest);

        return "Author id: "
                .concat(String.valueOf(id))
                .concat(" was deleted");
    }

    @PutMapping("authors/update/{id}")
    public String updateById(@PathVariable int id,
                             @RequestParam("key_parameter") String keyParameter,
                             @RequestParam("value_parameter") String valueParameter) {
        authorsSQLService.update(id, keyParameter, valueParameter);

        return "Author id: "
                .concat(String.valueOf(id))
                .concat(" was updated");

    }

}
