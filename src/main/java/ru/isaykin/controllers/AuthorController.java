package ru.isaykin.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.isaykin.reader.Author;
import ru.isaykin.services.AuthorService;

import java.util.List;
import java.util.Set;

@RestController
public class AuthorController {
    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping(value = "/newauthors")
    public ResponseEntity<?> create(@RequestBody Author author) {
        authorService.create(author);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/newauthors")
    public ResponseEntity<List<Author>> getAll() {
        final List<Author> authors = authorService.getAll();
        return authors != null && !authors.isEmpty()
                ?new ResponseEntity<>(authors, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/newauthors/{id}")
    public ResponseEntity<Author> getOneById(@PathVariable (name ="id") int id) {
        final Author author = authorService.getOneById(id);

        return author != null
                ? new ResponseEntity<>(author, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value =  "/newauthors/{id}")
    public ResponseEntity<?> update(@PathVariable(value = "id") int id,
                                    @RequestBody Author author) {
        final boolean updated = authorService.update(author,id);

        return updated
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
    @DeleteMapping(value = "/newauthors/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") int id) {
        final boolean deleted = authorService.delete(id);
        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

//    @GetMapping(value = "/newauthors")
//    public ResponseEntity<?> getListByName(@RequestParam(value = "first_name") String name){
//        final List<Author> byName = authorService.getAllByName(name);
//        return byName != null && !byName.isEmpty()
//        ?   new ResponseEntity<>(HttpStatus.OK)
//                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
//
//    }
}
