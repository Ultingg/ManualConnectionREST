package ru.isaykin.repository;


import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.isaykin.reader.Author;

@Component
@Repository
public interface AuthorRepo extends CrudRepository<Author, Long> {

    Author getById(@NonNull Long id);

    Author getByFirstNameAndLastName(String firstName, String LastName);

    void deleteById(Long id);


}
