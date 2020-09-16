package ru.isaykin.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.isaykin.reader.Author;
import ru.isaykin.repository.AuthorRepo;
import ru.isaykin.repository.AuthorsRepositorySQL;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

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
        return authorRepo.getAll();
    }


    public ResponseEntity<Author> getOneById(Long id) {
        ResponseEntity<Author> result;
        Author author = authorRepo.getById(id);
        if (author == null) {
            return result = new ResponseEntity<>(NOT_FOUND);
        } else {
            return result = new ResponseEntity<Author>(author, OK);
        }
    }


    public List<Author> getListByFirstNameAndLastName(String firstname, String lastname) {
        List<Author> authors = authorRepo.getAll();
        List<Author> selectedAuthors = new ArrayList<>();
        for (Author author : authors) {
            if ((author.getFirstName().equals(firstname)) || (author.getLastName().equals(lastname))) {
                selectedAuthors.add(author);
            }
        }

        return selectedAuthors;
    }


    public Author update(Long id, Author authorToUpdate) {

        Author oldAuthor = authorRepo.getById(id);
        if (oldAuthor == null) {
            return null;
        } else {
            authorToUpdate.setId(oldAuthor.getId());
            authorRepo.save(authorToUpdate);

            return authorToUpdate;
        }
    }

    public ResponseEntity<Author> delete(Long id) {
        ResponseEntity<Author> result;
        if (id == null) {
            return result = new ResponseEntity<>(NOT_FOUND);
        } else {
            authorRepo.deleteById(id);
            return result = new ResponseEntity<>(OK);
        }
        //authorsRepositorySQL.requestToTable(new StringBuilder("DELETE FROM authors WHERE id = ").append(id).toString());
    }


    public List<Author> getListByAgeGT(int age) {
        Date currentDateMinusYears = Date.
                valueOf(LocalDate.now().minusYears(age));

        return authorRepo.getListByAgeGraterThen(currentDateMinusYears);
    }

    public List<Author> getListByAgeLT(int age) {
        Date currentDateMinusYears = Date.
                valueOf(LocalDate.now().minusYears(age));

        return authorRepo.getListByAgeLessThen(currentDateMinusYears);
    }

    public String createList(List<Author> authorList) {
        return authorsRepositorySQL.createList(authorList);

    }
}

