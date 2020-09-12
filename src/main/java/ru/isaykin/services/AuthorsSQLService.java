package ru.isaykin.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.isaykin.exceptions.NotFoundException;
import ru.isaykin.reader.Author;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static java.lang.String.valueOf;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@Service
@Component
public class AuthorsSQLService implements AuthorService {

    private final AuthorsRepositorySQL authorsRepositorySQL;

    public AuthorsSQLService(AuthorsRepositorySQL authorsRepositorySQL) {
        this.authorsRepositorySQL = authorsRepositorySQL;
    }

    @Override
    public Author update(Author author, int id) {
        String selectionRequest = format("SELECT * FROM authors WHERE id = %d;", id);

        String updateRequest = format(
                "UPDATE authors SET first_name = '%s', last_name = '%s', email = '%s', birthdate = '%tF' WHERE id = %d;",
                author.getFirstName(), author.getLastName().replaceAll("'", "\\\\\'"),
                author.getEmail(), author.getBirthDate(), id);

        authorsRepositorySQL.requestToTable(updateRequest);

        List<Author> authorList = authorsRepositorySQL.getListOfAuthors(selectionRequest);


        return authorList.get(0);
    }

    @Override
    public Author create(Author author) {

        String creationRequest = format("INSERT INTO authors (first_name, last_name, email, birthdate) VALUES " +
                        "( '%s', '%s', '%s', '%tF')",
                author.getFirstName(), author.getLastName().replaceAll("'", "\\\\\'"), author.getEmail(), author.getBirthDate());
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
        if (author1 != null) return author1;

        return author;

    }

    @Override
    public List<Author> getAll() {
        return authorsRepositorySQL.getAll();
    }

    @Override
    @ResponseStatus(code = NOT_FOUND)
    public Author getOneById(int id) {
        List<Author> authorList = authorsRepositorySQL.getAll();
        return authorList.stream()
                .filter(author -> author.getId() == id)
                .findFirst().orElseThrow(NotFoundException::new);
    }


    public List<Author> getByFirstNameAndLastName(String firstname, String lastname) {
        List<Author> authors = authorsRepositorySQL.getAll();
        List<Author> selectedAuthors = new ArrayList<>();
        for (Author author : authors) {
            if ((author.getFirstName().equals(firstname)) || (author.getLastName().equals(lastname))) {
                selectedAuthors.add(author);
            }
        }
        return selectedAuthors;
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
        authorsRepositorySQL.requestToTable(new StringBuilder("DELETE FROM authors WHERE id = ").append(id).toString());
        return true;
    }


    public List<Author> getListByAge(int age) {
        return authorsRepositorySQL.getAuthorsWithAge(age);
    }

    public String createList(List<Author> authorList) {
        return authorsRepositorySQL.createList(authorList);

    }
}

