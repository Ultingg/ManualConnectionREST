package ru.isaykin.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.isaykin.reader.Author;
import ru.isaykin.services.AuthorsSQLService;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

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
    public Author getOneAuthor(@PathVariable Long id) {
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

    @PostMapping("authors/addlist")
    public ResponseEntity<Author> createList(@RequestBody List<Author> authorList) {
        ResponseEntity<Author> result;

        if (authorList == null) {
            result = new ResponseEntity<>(NO_CONTENT);
        } else {
            String resultMassage = authorsSQLService.createList(authorList);
            result = new ResponseEntity(resultMassage, OK);
        }
        return result;
    }


    @DeleteMapping("authors/{id}")
    public StringBuilder delete(@PathVariable Long id) {
        authorsSQLService.delete(id);

        return new StringBuilder("Author id: ").append(id).append(" was deleted");

    }

    @PutMapping("authors/{id}")
    public ResponseEntity<Author> updateById(@PathVariable Long id,
                                             @RequestBody Author author) {
        ResponseEntity<Author> result;
        if (author == null) {
            result = new ResponseEntity<>(NOT_MODIFIED);
        } else {
            Author author1 = authorsSQLService.update(author, id);
            result = new ResponseEntity<>(author1, OK);
        }

        return result;

    }

}
