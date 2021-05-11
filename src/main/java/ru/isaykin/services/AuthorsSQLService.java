package ru.isaykin.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.isaykin.exceptions.AuthorNotFoundException;
import ru.isaykin.exceptions.IllegalArgumentAuthorException;
import ru.isaykin.model.Author;
import ru.isaykin.repository.AuthorRepo;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.sql.Date.valueOf;
import static java.time.LocalDate.now;

@Slf4j
@Service
public class AuthorsSQLService {

    private final AuthorRepo authorRepo;

    public AuthorsSQLService(AuthorRepo authorRepo) {
        this.authorRepo = authorRepo;
    }

    public List<Author> getAll() {
        return authorRepo.getAll();
    }

    public Author getOneById(Long id) {
        Author result = authorRepo.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException("Author not found."));
        return result;
    }

    public List<Author> getListByFirstNameAndLastNameOrNull(String firstName, String lastName) {
        List<Author> selectedAuthors = new ArrayList<>();
        if (firstName == null && lastName == null) {
            authorRepo.findAll().iterator().forEachRemaining(selectedAuthors::add);
        } else {
            List<Author> authors = new ArrayList<>();
            authorRepo.findAll().iterator().forEachRemaining(authors::add);
            selectedAuthors = authors.stream()
                    .filter(author -> author.getFirstName().equals(firstName) || author.getLastName().equals(lastName))
                    .collect(Collectors.toList());
            if (selectedAuthors.isEmpty()) {
                throw new AuthorNotFoundException("Authors not found.");
            }
        }
        return selectedAuthors;
    }


    public Author update(Long id, Author authorToUpdate) {
        if (!authorRepo.existsById(id)) throw new AuthorNotFoundException("Author not found");
        authorToUpdate.setId(id);
        authorRepo.save(authorToUpdate);
        return authorToUpdate;
    }

    public void deleteById(Long id) {
        if (!authorRepo.existsById(id)) {
            throw new AuthorNotFoundException("Author not found.");
        }
        authorRepo.deleteById(id);
    }

    public List<Author> getListByAgeGT(Integer age) {
        if (age == null || age < 0) {
            throw new IllegalArgumentAuthorException("You've sent illegal argument. Age is not correct");
        }
        Date currentDateMinusYears = valueOf(now().minusYears(age));
        List<Author> resultList = authorRepo.getListByAgeGraterThen(currentDateMinusYears);
        if (resultList.isEmpty()) {
            throw new AuthorNotFoundException("Authors not found.");
        }
        return resultList;
    }

    public List<Author> getListByAgeLT(Integer age) {
        if (age == null || age < 0) {
            throw new IllegalArgumentAuthorException("You've sent illegal argument. Age is not correct");
        }
        Date currentDateMinusYears = valueOf(now().minusYears(age));
        List<Author> resultList = authorRepo.getListByAgeLessThen(currentDateMinusYears);
        if (resultList.isEmpty()) {
            throw new AuthorNotFoundException("Author not found.");
        }
        return resultList;
    }

    public Author insertAuthor(Author author) {
        if (author == null) {
            throw new IllegalArgumentAuthorException("You've sent illegal argument. Wrong argument could't be inserted to database.");
        }
        Author result;
        try {
            result = authorRepo.save(author);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentAuthorException("You've sent illegal argument. Wrong argument could't be inserted to database.");
        }

        return result;

    }

    public List<Author> insertMany(List<Author> authorList) {
        List<Author> insertedAuthors = new ArrayList<>();
        authorRepo.saveAll(authorList).iterator().forEachRemaining(insertedAuthors::add);
        return insertedAuthors;
    }

}

