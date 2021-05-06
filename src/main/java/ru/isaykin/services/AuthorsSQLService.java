package ru.isaykin.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.isaykin.exceptions.AuthorNotFoundException;
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
        List<Author> authors = new ArrayList<>();
        authorRepo.findAll().iterator().forEachRemaining(authors::add);
        List<Author> selectedAuthors = authors.stream()
                .filter(author -> author.getFirstName().equals(firstName) || author.getLastName().equals(lastName))
                .collect(Collectors.toList());
        return selectedAuthors.size() != 0 ? selectedAuthors : null;
    }


    public Author update(Long id, Author authorToUpdate) {
        if(!authorRepo.existsById(id)) throw  new AuthorNotFoundException("Author not found");
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
        Date currentDateMinusYears = valueOf(now().minusYears(age));
        return authorRepo.getListByAgeGraterThen(currentDateMinusYears);
    }

    public List<Author> getListByAgeLT(Integer age) {
        Date currentDateMinusYears = valueOf(now().minusYears(age));
        return authorRepo.getListByAgeLessThen(currentDateMinusYears);
    }

    public Author insertAuthor(Author author) {
        return authorRepo.save(author);

    }

    public List<Author> insertMany(List<Author> authorList) {
        List<Author> insertedAuthors = new ArrayList<>();
        authorRepo.saveAll(authorList).iterator().forEachRemaining(insertedAuthors::add);
        return insertedAuthors;
    }

}

