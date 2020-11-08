package ru.isaykin.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.isaykin.reader.Author;
import ru.isaykin.reader.AuthorList;
import ru.isaykin.repository.AuthorRepo;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Date.valueOf;
import static java.time.LocalDate.now;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@Service
@Component

public class AuthorsSQLService {

    private final AuthorRepo authorRepo;


    public AuthorsSQLService(AuthorRepo authorRepo) {
        this.authorRepo = authorRepo;
    }

    public List<Author> getAll() {
        return authorRepo.getAll();
    }

    public ResponseEntity<Author> getOneById(Long id) {
        Author author = authorRepo.getById(id);
        ResponseEntity<Author> result;
        if (author == null) {
            result = new ResponseEntity<>(NOT_FOUND);
        } else {
            result = new ResponseEntity<>(author, OK);
        }

        return result;
    }

    public List<Author> getListByFirstNameAndLastNameOrNull(String firstName, String lastName) {
        List<Author> authors = authorRepo.getAll();
        List<Author> selectedAuthors = new ArrayList<>();
        for (Author author : authors) {
            if ((author.getFirstName().equals(firstName)) || (author.getLastName().equals(lastName))) {
                selectedAuthors.add(author);
            }
        }
        return selectedAuthors.size() != 0 ? selectedAuthors : null;
    }


    public Author update(Long id, Author authorToUpdate) {
        Author oldAuthor = authorRepo.getById(id);
        if (oldAuthor == null) {
            return null;
        }
        authorToUpdate.setId(oldAuthor.getId());
        authorRepo.save(authorToUpdate);
        return authorToUpdate;
    }

    public ResponseEntity<Author> delete(Long id) {
        Author authorChecking = authorRepo.getById(id);
        ResponseEntity<Author> result;
        if (authorChecking == null) {
            result = new ResponseEntity<>(NOT_FOUND);
        } else {
            authorRepo.deleteById(id);
            result = new ResponseEntity<>(OK);
        }
        return result;
    }

    public List<Author> getListByAgeGT(Integer age) {
        Date currentDateMinusYears = valueOf(now().minusYears(age));

        return authorRepo.getListByAgeGraterThen(currentDateMinusYears);
    }

    public List<Author> getListByAgeLT(Integer age) { // try catch NPException???
        Date currentDateMinusYears = valueOf(now().minusYears(age));

        return authorRepo.getListByAgeLessThen(currentDateMinusYears);
    }

    public Author insert(Author author) {
        authorRepo.insert(author.getFirstName(),
                author.getLastName(),
                author.getEmail(),
                valueOf(author.getBirthdate()));

        return authorRepo.getByEmail(author.getEmail());
    }

    public AuthorList<Author> insertMany(AuthorList<Author> authorList) {
        AuthorList<Author> insertedAuthors = new AuthorList<>();
        for (Author author : authorList) {
            authorRepo.insert(author.getFirstName(),
                    author.getLastName(),
                    author.getEmail(),
                    valueOf(author.getBirthdate()));
            insertedAuthors.add(authorRepo.getByEmail(author.getEmail()));
        }
        authorList.clear();
        authorList.addAll(insertedAuthors);
        return authorList;
    }

}

