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
    public Object getListOrGetOneByFirstNameAndLastName(@RequestParam(value = "first_name", required = false) String firstName,
                                                              @RequestParam(value = "last_name", required = false) String lastName) {
        if (firstName != null || lastName != null) {
            return authorsSQLService.getListByFirstNameAndLastName(firstName, lastName);
        } else {
            return authorsSQLService.getAll();
        }
    }


    @GetMapping("authors/{id}")
    public ResponseEntity<Author> getOneAuthor(@PathVariable Long id) {
        return authorsSQLService.getOneById(id);
    }


    @GetMapping("authors/age/gt/{age}")
    public List<Author> getListByAgeGraterThen(@PathVariable int age) {
        return authorsSQLService.getListByAgeGT(age);
    }

    @GetMapping("authors/age/ls/{age}")
    public List<Author> getListByAgeLessThen(@PathVariable int age) {
        return authorsSQLService.getListByAgeLT(age);
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
    public ResponseEntity<String> createList(@RequestBody List<Author> authorList) {
        ResponseEntity<String> result;

        if (authorList == null) {
            result = new ResponseEntity<>(NO_CONTENT);
        } else {
            String resultMassage = authorsSQLService.createList(authorList);
            result = new ResponseEntity<>(resultMassage, OK);
        }
        return result;
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
