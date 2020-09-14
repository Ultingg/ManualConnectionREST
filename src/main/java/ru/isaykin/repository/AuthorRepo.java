package ru.isaykin.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.isaykin.reader.Author;

@Repository
public interface AuthorRepo extends CrudRepository<Author, Long> {

    Author getById(Long id);
}
