package ru.isaykin.controllers;


import org.springframework.web.bind.annotation.*;
import ru.isaykin.databaseactions.updateRequest;
import ru.isaykin.exceptions.NotFoundException;
import ru.isaykin.reader.Author;
import ru.isaykin.reader.DataBaseRepository;

import java.util.Set;

import static ru.isaykin.reader.PropetiesRepo.getDataForPropRepo;

@RestController
@RequestMapping
public class MyRestController {


    @GetMapping("authors")
    public Set<Author> getList() {
        Set<Author> authors = DataBaseRepository.getAllAuthors();

        return authors;
    }

    @GetMapping("authors/{id}")
    public Author getOneAuthor(@PathVariable int id) {
        Set<Author> authors = DataBaseRepository.getAllAuthors();
        return authors.stream()
                .filter(author -> author.getId() == id)
                .findFirst().orElseThrow(NotFoundException::new);
    }

    @GetMapping("authors/")
    public Author getOneAuthorByNameOrSurname(@RequestParam(value = "firstname") String name,
                                              @RequestParam(value = "lastname") String surname) {
        Set<Author> authors = DataBaseRepository.getAllAuthors();
        return authors.stream().filter(author -> author.getFirstName().equals(name) && author.getLastName().equals(surname))
                .findFirst()
                .orElseThrow(NotFoundException::new);
    }

    @GetMapping("authors/age/gt/{age}")
    public Set<Author> getListByAge(@PathVariable int age) {
        getDataForPropRepo();
        Set<Author> authors = DataBaseRepository.getAuthorsWithAge(age);
        return authors;
    }

    @PostMapping("authors/insert")
    public String insertAuthorToTable(@RequestParam(value = "first_name") String firstname,
                                      @RequestParam(value = "last_name") String lastname,
                                      @RequestParam(value = "email") String email,
                                      @RequestParam(value = "birthdate") String birthdate) {
        String insertRequest = "INSERT authors (first_name, last_name, email, birthdate) VALUES (\"" + firstname
                + "\" ,\"" + lastname + "\" ,\"" + email + "\" ,\"" + birthdate + "\")";
        updateRequest.UpdateTable(insertRequest);
        return "added";
    }
    @DeleteMapping("authors/{id}")
    public String delete(@PathVariable String id) {
        String deleteRequest = "DELETE FROM authors WHERE id = " + id;
        updateRequest.UpdateTable(deleteRequest);
        return "Author id: " + id + " was deleted";
    }
    @PutMapping("authors/update/{id}")
    public String updateById(@PathVariable int id, @RequestParam(value = "first_name") String firstname) {
        String updateRequestString = "UPDATE authors SET first_name = \"" + firstname + "\" WHERE id = \"" + id + "\"";
        updateRequest.UpdateTable(updateRequestString);
        return "Author id: " + id + " was updated";

    }

}
