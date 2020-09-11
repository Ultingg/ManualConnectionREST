package ru.isaykin.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.isaykin.exceptions.NotFoundException;
import ru.isaykin.reader.Author;
import ru.isaykin.reader.DataBaseRepository;

import java.util.List;

import static java.lang.String.format;
import static java.lang.String.valueOf;
import static ru.isaykin.reader.PropertiesRepo.getDataForPropRepo;
@Slf4j
@Service
@Component
public class AuthorsSQLService implements AuthorService {

    private final DataBaseRepository dataBaseRepository;
    private final AuthorsRepositorySQL authorsRepositorySQL;

    public AuthorsSQLService(DataBaseRepository dataBaseRepository, AuthorsRepositorySQL authorsRepositorySQL) {
        this.dataBaseRepository = dataBaseRepository;
        this.authorsRepositorySQL = authorsRepositorySQL;
    }

    @Override
    public Author create(Author author) {

            String creationRequest = format("INSERT INTO authors (first_name, last_name, email, birthdate) VALUES " +
                    "( '%s', '%s', '%s', '%tF')",
                    author.getFirstName(), author.getLastName().replaceAll("'","\\\\\'"), author.getEmail(), author.getBirthDate());
            authorsRepositorySQL.requestToTable(creationRequest);

            String feedbackRequest = format("SELECT * FROM authors WHERE first_name = '%s' AND last_name = '%s';",
                    author.getFirstName(), author.getLastName().replaceAll("'", "\\\\\'"));
        Author author1 = new Author();
           try {
               List<Author> authors = authorsRepositorySQL.getListOfAuthors(feedbackRequest);
                author1 = authors.get(0);
           } catch (NullPointerException e) {
               log.error(e.getMessage() + "you get List authors = null. Wrong request to DB");

           }
        if(author1 != null) return author1;

        return author;

    }

    @Override
    public List<Author> getAll() {
        return dataBaseRepository.getAllAuthors();
    }

    @Override
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public Author getOneById(int id) {
        List<Author> authorList = dataBaseRepository.getAllAuthors();
        return authorList.stream()
                .filter(author -> author.getId() == id)
                .findFirst().orElseThrow(NotFoundException::new);
    }

    @Override
    public boolean update(Author author, int id) {
        return false;
    }


    public Author getByFirstNameAndLastName(String firstname, String lastname) {
        List<Author> authors = dataBaseRepository.getAllAuthors();
        return authors.stream().filter(author -> author.getFirstName().equals(firstname) || author.getLastName().equals(lastname))
                .findFirst()
                .orElseThrow(NotFoundException::new);
    }

    public boolean update(int id, String keyParameter, String valueParameter) {

        String updateRequestString = "UPDATE authors SET "
                .concat(keyParameter)
                .concat(" = \"")
                .concat(valueParameter)
                .concat("\" WHERE id = \"")
                .concat(valueOf(id))
                .concat("\"");
        authorsRepositorySQL.requestToTable(updateRequestString);
        return true;
    }

    @Override
    public boolean delete(int id) {
        String deleteRequest = "DELETE FROM authors WHERE id = ".concat(valueOf(id));
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

