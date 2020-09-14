package ru.isaykin.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.isaykin.reader.Author;

@Component
@Repository
public interface AuthorRepo extends CrudRepository<Author, Long> {

    Author getById(Long id);
}
