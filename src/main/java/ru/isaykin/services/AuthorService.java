package ru.isaykin.services;

import ru.isaykin.reader.Author;

import java.util.List;

public interface AuthorService {

    void create(Author author);

    List<Author> getAll();

    Author getOneById(int id);

    boolean update(Author author, int id);

    boolean delete(int id);

    Author getByFirstNameAndLastName(String first_name, String last_name);

}
