package ru.isaykin.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.isaykin.exceptions.NotFoundException;
import ru.isaykin.reader.Author;
import ru.isaykin.reader.DataBaseRepository;

import java.util.List;

import static ru.isaykin.reader.PropetiesRepo.getDataForPropRepo;

@Component
public class AuthorsSQLService implements AuthorService {

    private final DataBaseRepository dataBaseRepository;
    private final AuthorsRepositorySQL authorsRepositorySQL;

    public AuthorsSQLService(DataBaseRepository dataBaseRepository, AuthorsRepositorySQL authorsRepositorySQL) {
        this.dataBaseRepository = dataBaseRepository;
        this.authorsRepositorySQL = authorsRepositorySQL;
    }

    @Override
    public void create(Author author) {
        List<Author> authorList = dataBaseRepository.getAllAuthors();
    }

    @Override
    public List<Author> getAll() {
        return  dataBaseRepository.getAllAuthors();
    }

    @Override
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public Author getOneById(int id) {
        List<Author> authorSet = dataBaseRepository.getAllAuthors();
        return authorSet.stream()
                .filter(author -> author.getId() == id)
                .findFirst().orElseThrow(NotFoundException::new);
    }

    @Override
    public boolean update(Author author, int id) {
        return false;
    }


    public Author getByFirstNameAndLastName(String firstname, String lastname) {
        List<Author> authors = dataBaseRepository.getAllAuthors();
        return authors.stream().filter(author -> author.getFirstName().equals(firstname) && author.getLastName().equals(lastname))
                .findFirst()
                .orElseThrow(NotFoundException::new);
    }


    public boolean update(int id, String keyParameter, String valueParameter) {

        String updateRequestString = "UPDATE authors SET "
                .concat(keyParameter)
                .concat(" = \"")
                .concat(valueParameter)
                .concat("\" WHERE id = \"")
                .concat(String.valueOf(id))
                .concat("\"");
        authorsRepositorySQL.requestToTable(updateRequestString);
        return true;
    }

    @Override
    public boolean delete(int id) {
        String deleteRequest = "DELETE FROM authors WHERE id = ".concat(String.valueOf(id));
        authorsRepositorySQL.requestToTable(deleteRequest);
        return true;
    }


    public List<Author> getListByAge(int age) {
        getDataForPropRepo();
        return dataBaseRepository.getAuthorsWithAge(age);
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
        authorsRepositorySQL.requestToTable(insertRequest);
        return "added";
    }


}

