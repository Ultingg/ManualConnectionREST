package ru.isaykin.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.isaykin.reader.Author;
import ru.isaykin.services.AuthorService;

import java.util.List;

import static org.springframework.http.HttpStatus.*;


@RestController
public class AuthorController {
    private final AuthorService authorService;

    @Autowired
    public AuthorController(@Qualifier("JAP") AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping("/newauthors")
    public ResponseEntity<?> create(@RequestBody Author author) {
        authorService.create(author);
        return new ResponseEntity<>(CREATED);
    }

    @GetMapping("/newauthors")
    public ResponseEntity<List<Author>> getAll() {
        final List<Author> authors = authorService.getAll();
        if ((authors != null) && (!authors.isEmpty()))
            return new ResponseEntity<>(authors, OK);
        else {
            return new ResponseEntity<>(NOT_FOUND);
        }


    }

    @GetMapping("/newauthors/{id}")
    public ResponseEntity<Author> getOneById(@PathVariable("id") int id) {
        final Author author = authorService.getOneById(id);

        if (author != null) return new ResponseEntity<>(author, OK);
        else return new ResponseEntity<>(NOT_FOUND);
    }

    @PutMapping("/newauthors/{id}")
    public ResponseEntity<?> update(@PathVariable("id") int id,
                                    @RequestBody Author author) {
        final boolean updated = authorService.update(author, id);
        if (updated) return new ResponseEntity<>(OK);
        else return new ResponseEntity<>(NO_CONTENT);
    }

    @DeleteMapping("/newauthors/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id) {
        final boolean deleted = authorService.delete(id);
        if (deleted) return new ResponseEntity<>(OK);
        else return new ResponseEntity<>(NO_CONTENT);
    }

//    @GetMapping(value = "/newauthors/byname")
//    public ResponseEntity<?> getListByName(@RequestParam(value = "first_name") String name){
//        final List<Author> byName = authorService.getAllByName(name);
//        return byName != null && !byName.isEmpty()
//        ?   new ResponseEntity<>(HttpStatus.OK)
//                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
//
//    }
}
