package ru.isaykin.repository;


import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.isaykin.model.Author;

import java.sql.Date;
import java.util.List;

@Component
@Repository
public interface AuthorRepo extends CrudRepository<Author, Long> {

    void deleteById(Long id);

    @Query("SELECT * FROM authors WHERE birthdate <= :date")
    List<Author> getListByAgeGraterThen(@Param("date") Date date);

    @Query("SELECT * FROM authors WHERE birthdate >= :date")
    List<Author> getListByAgeLessThen(@Param("date") Date date);

    @Query("SELECT * FROM authors")
    List<Author> getAll();

}
