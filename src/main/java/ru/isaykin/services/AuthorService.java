package ru.isaykin.services;

import ru.isaykin.reader.Author;

import java.util.List;

public interface AuthorService {

    Author create(Author author);

    List<Author> getAll();

    Author getOneById(int id);

    Author update(Author author, int id);

    boolean delete(int id);


}
