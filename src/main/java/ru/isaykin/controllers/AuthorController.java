package ru.isaykin.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.isaykin.reader.Author;
import ru.isaykin.services.AuthorsSQLService;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

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
        ResponseEntity<Object> result;
        if (firstName != null || lastName != null) {
            List<Author> resultList = authorsSQLService.getListByFirstNameAndLastNameOrNull(firstName, lastName);
            if (resultList != null) {
                result = new ResponseEntity<>(resultList, OK);
            } else {
                result = new ResponseEntity<>(NOT_FOUND);
            }
        } else {
            result = new ResponseEntity<>(authorsSQLService.getAll(), OK);
        }
        return result;
    }


    @GetMapping("authors/{id}")
    public ResponseEntity<Author> getOneAuthor(@PathVariable Long id) {
        return authorsSQLService.getOneById(id);
    }


    @GetMapping("authors/age/gt/{age}")
    public ResponseEntity<List<Author>> getListByAgeGraterThen(@PathVariable Integer age) {
        List<Author> authorList = authorsSQLService.getListByAgeGT(age);
        if (authorList.isEmpty()) {
            return new ResponseEntity<>(NOT_FOUND);
        } else {
            return new ResponseEntity<>(authorList, OK);
        }
    }

    @GetMapping("authors/age/lt/{age}")
    public ResponseEntity<List<Author>> getListByAgeLessThen(@PathVariable Integer age) {
        List<Author> authorList = authorsSQLService.getListByAgeLT(age);
        if (authorList.isEmpty()) {
            return new ResponseEntity<>(NOT_FOUND);
        } else {
            return new ResponseEntity<>(authorList, OK);
        }
    }

    @PostMapping("authors")
    public ResponseEntity<Author> insert(@RequestBody Author author) {
        ResponseEntity<Author> result;

        if (author == null) {
            result = new ResponseEntity<>(NO_CONTENT);
        } else {
            Author author1 = authorsSQLService.insert(author);
            result = new ResponseEntity<>(author1, OK);
        }

        return result;
    }

    @PostMapping("authors/addlist")
    public ResponseEntity<List<Author>> createList(@RequestBody List<Author> authorList) {
        List<Author> insertedList;
        if (authorList == null) {
            return new ResponseEntity<>(NO_CONTENT);
        } else {
            insertedList = authorsSQLService.insertMany(authorList);
            return new ResponseEntity<>(insertedList, OK);
        }
    }


    @DeleteMapping("authors/{id}")
    public ResponseEntity<Author> delete(@PathVariable Long id) {


        return authorsSQLService.delete(id);

    }

    @PutMapping("authors/{id}")
    public ResponseEntity<Author> updateById(@PathVariable Long id,
                                             @RequestBody Author author) {
        ResponseEntity<Author> result;
        Author updatedAuthor = authorsSQLService.update(id, author);
        if (updatedAuthor == null) {
            result = new ResponseEntity<>(NOT_MODIFIED);
        } else {
            result = new ResponseEntity<>(updatedAuthor, OK);
        }

        return result;

    }

}
