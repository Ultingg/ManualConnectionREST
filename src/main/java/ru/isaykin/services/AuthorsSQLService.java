package ru.isaykin.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.isaykin.reader.Author;
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
        if (author == null) {
            return new ResponseEntity<>(NOT_FOUND);
        } else {
            return new ResponseEntity<>(author, OK);
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
        }
        authorToUpdate.setId(oldAuthor.getId());
        authorRepo.save(authorToUpdate);
        return authorToUpdate;
    }

    public ResponseEntity<Author> delete(Long id) {
        if (id == null) {
            return new ResponseEntity<>(NOT_FOUND);
        }
        authorRepo.deleteById(id);
        return new ResponseEntity<>(OK);
    }

    public List<Author> getListByAgeGT(int age) {
        Date currentDateMinusYears = valueOf(now().minusYears(age));

        return authorRepo.getListByAgeGraterThen(currentDateMinusYears);
    }

    public List<Author> getListByAgeLT(int age) {
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

    public List<Author> insertMany(List<Author> authorList) {
        List<Author> insertedAuthors = new ArrayList<>();
        for (Author author : authorList) {
            authorRepo.insert(author.getFirstName(),
                    author.getLastName(),
                    author.getEmail(),
                    valueOf(author.getBirthdate()));
            insertedAuthors.add(authorRepo.getByEmail(author.getEmail()));
        }
        return insertedAuthors;
    }

    public List<Author> insertManyToSortedAuthors(List<Author> authorList) {
        List<Author> insertedAuthors = new ArrayList<>();
        for (Author author : authorList) {
            authorRepo.insert(author.getFirstName(),
                    author.getLastName(),
                    author.getEmail(),
                    valueOf(author.getBirthdate()));
            insertedAuthors.add(authorRepo.getByEmail(author.getEmail()));
        }
        return insertedAuthors;
    }
}

