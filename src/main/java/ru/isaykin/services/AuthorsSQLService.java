package ru.isaykin.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.isaykin.reader.Author;
import ru.isaykin.repository.AuthorRepo;
import ru.isaykin.repository.AuthorsRepositorySQL;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static java.lang.String.valueOf;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@Service
@Component
public class AuthorsSQLService {

    private final AuthorsRepositorySQL authorsRepositorySQL;
     private final AuthorRepo authorRepo;


    public AuthorsSQLService(AuthorsRepositorySQL authorsRepositorySQL, AuthorRepo authorRepo) {
        this.authorsRepositorySQL = authorsRepositorySQL;

        this.authorRepo = authorRepo;
    }

    public Author update(Author author, Long id) {
        String selectionRequest = format("SELECT * FROM authors WHERE id = %d;", id);

        String updateRequest = format(
                "UPDATE authors SET first_name = '%s', last_name = '%s', email = '%s', birthdate = '%tF' WHERE id = %d;",
                author.getFirstName(), author.getLastName().replaceAll("'", "\\\\\'"),
                author.getEmail(), author.getBirthdate(), id);

        authorsRepositorySQL.requestToTable(updateRequest);

        List<Author> authorList = authorsRepositorySQL.getListOfAuthors(selectionRequest);


        return authorList.get(0);
    }

    public Author create(Author author) {

        String creationRequest = format("INSERT INTO authors (first_name, last_name, email, birthdate) VALUES " +
                        "( '%s', '%s', '%s', '%tF')",
                author.getFirstName(), author.getLastName().replaceAll("'", "\\\\\'"), author.getEmail(), author.getBirthdate());
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

    public List<Author> getAll() {
        return authorsRepositorySQL.getAll();
    }

    @ResponseStatus(code = NOT_FOUND)
    public Author getOneById(Long id) {
       return authorRepo.getById(id);
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

    public boolean update(Long id, String keyParameter, String valueParameter) {

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

    public boolean delete(Long id) {
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

