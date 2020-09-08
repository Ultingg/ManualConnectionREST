package ru.isaykin.services;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.isaykin.exceptions.NotFoundException;
import ru.isaykin.reader.Author;
import ru.isaykin.reader.DataBaseRepository;

import java.util.List;
import java.util.Set;

import static ru.isaykin.reader.PropetiesRepo.getDataForPropRepo;

public class AuthorsServicSQL implements AuthorService {

private final DataBaseRepository dataBaseRepository;

    public AuthorsServicSQL(DataBaseRepository dataBaseRepository) {
        this.dataBaseRepository = dataBaseRepository;
    }

    @Override
    public void create(Author author) {
        Set<Author> authorSet = dataBaseRepository.getAllAuthors();
    }

    @Override
    public List<Author> getAll() {
        return (List<Author>) dataBaseRepository.getAllAuthors();
    }

    @Override
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public Author getOneById(int id) {
        Set<Author> authorSet = dataBaseRepository.getAllAuthors();
        return authorSet.stream()
                .filter(author -> author.getId() == id)
                .findFirst().orElseThrow(NotFoundException::new);
    }


    public Author getByFirstNameAndLastName(String firstname, String lastname) {


        Set<Author> authors = dataBaseRepository.getAllAuthors();
        return authors.stream().filter(author -> author.getFirstName().equals(firstname) && author.getLastName().equals(lastname))
                .findFirst()
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public boolean update(Author author, int id) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        String deleteRequest = "DELETE FROM authors WHERE id = ".concat(String.valueOf(id));
        AuthorsRepositorySQL.requestToTable(deleteRequest);
        return true;
    }


    public Set<Author> getListByAge(int age) {
        getDataForPropRepo();
        Set<Author> authors = dataBaseRepository.getAuthorsWithAge(age);
        return authors;
    }

    public String insertAuthorToTable(String firstname, String lastname, String email, String birthdate) {
        String insertRequest = "INSERT authors (first_name, last_name, email, birthdate) VALUES (\""
                .concat(firstname)
                .concat("\" ,\"")
                .concat(lastname)
                .concat("\" ,\"")
                .concat(email)
                .concat("\" ,\"")
                .concat(birthdate)
                .concat("\")");
        AuthorsRepositorySQL.requestToTable(insertRequest);
        return "added";
    }


}

