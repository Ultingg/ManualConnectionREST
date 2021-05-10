package ru.isaykin.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.isaykin.model.Author;
import ru.isaykin.services.AuthorsSQLService;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping
public class AuthorController {

    private final AuthorsSQLService authorsSQLService;

    public AuthorController(AuthorsSQLService authorsSQLService) {
        this.authorsSQLService = authorsSQLService;
    }

    @GetMapping("authors")
    public ResponseEntity<Object> getListOrGetOneByFirstNameAndLastName(@RequestParam(value = "first_name", required = false) String firstName,
                                                                        @RequestParam(value = "last_name", required = false) String lastName) {
        List<Author> resultList = authorsSQLService.getListByFirstNameAndLastNameOrNull(firstName, lastName);
        return new ResponseEntity<>(resultList, OK);
    }


    @GetMapping("authors/{id}")
    public ResponseEntity<Author> getOneAuthor(@PathVariable Long id) {
        Author author = authorsSQLService.getOneById(id);
        return new ResponseEntity<>(author, OK);
    }


    @GetMapping("authors/age/gt/{age}")
    public ResponseEntity<List<Author>> getListByAgeGraterThen(@PathVariable Integer age) {
        List<Author> authorList = authorsSQLService.getListByAgeGT(age);
        return new ResponseEntity<>(authorList, OK);
    }

    @GetMapping("authors/age/lt/{age}")
    public ResponseEntity<List<Author>> getListByAgeLessThen(@PathVariable Integer age) {
        List<Author> authorList = authorsSQLService.getListByAgeLT(age);
        return new ResponseEntity<>(authorList, OK);
    }


    @PostMapping(value = "authors")
    public ResponseEntity<Author> insert(@Valid @RequestBody Author author) {
        Author author1 = authorsSQLService.insertAuthor(author);
        return new ResponseEntity<>(author1, OK);
    }

    @PostMapping("authors/addlist")
    public ResponseEntity<List<Author>> insertList(@Valid @RequestBody List<Author> authorList) {
        List<Author> insertedList = authorsSQLService.insertMany(authorList);
        return new ResponseEntity<>(insertedList, OK);
    }


    @DeleteMapping("authors/{id}")
    public ResponseEntity<Author> delete(@PathVariable Long id) {
        authorsSQLService.deleteById(id);
        return new ResponseEntity<>(NO_CONTENT);
    }

    @PutMapping("authors/{id}")
    public ResponseEntity<Author> updateById(@PathVariable Long id,
                                             @Valid @RequestBody Author author) {
        Author updatedAuthor = authorsSQLService.update(id, author);
        return new ResponseEntity<>(updatedAuthor, OK);

    }

}
