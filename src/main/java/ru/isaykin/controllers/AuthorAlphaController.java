package ru.isaykin.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.isaykin.reader.Author;
import ru.isaykin.services.AuthorsSQLService;

import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping
public class AuthorAlphaController {

    private final AuthorsSQLService authorsSQLService;

    public AuthorAlphaController(AuthorsSQLService authorsSQLService) {
        this.authorsSQLService = authorsSQLService;
    }


    @GetMapping("authors")
    public List<Author> getListOrGetOneByFirstNameAndLastName(@RequestParam(value = "first_name", required = false) String firstName,
                                                              @RequestParam(value = "last_name", required = false) String lastName) {
        if (firstName != null || lastName != null) {
            return authorsSQLService.getByFirstNameAndLastName(firstName, lastName);
        } else {
            return authorsSQLService.getAll();
        }
    }

    @GetMapping("authors/{id}")
    public Author getOneAuthor(@PathVariable int id) {
        return authorsSQLService.getOneById(id);

    }


    @GetMapping("authors/age/gt/{age}")
    public List<Author> getListByAge(@PathVariable int age) {
        return authorsSQLService.getListByAge(age);
    }


    @PostMapping("authors")
    public ResponseEntity<Author> create(@RequestBody Author author) {
        ResponseEntity<Author> result;
        if (author == null) {
            result = new ResponseEntity<>(NO_CONTENT);
        } else {
            Author author1 = authorsSQLService.create(author);
            result = new ResponseEntity<>(author1, OK);
        }

        return result;
    }


    @DeleteMapping("authors/{id}")
    public String delete(@PathVariable int id) {
        authorsSQLService.delete(id);

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
