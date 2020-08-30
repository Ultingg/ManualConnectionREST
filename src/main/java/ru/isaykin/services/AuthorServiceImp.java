package ru.isaykin.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.isaykin.reader.Author;

import java.util.List;
import java.util.Set;
@Service
public class AuthorServiceImp implements AuthorService{
    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public void create(Author author) {
        authorRepository.save(author);
    }

    @Override
    public List<Author> getAll() {
        return authorRepository.findAll();
    }

    @Override
    public Author getOneById(int id) {
        return authorRepository.getOne(id);
    }

    @Override
    public Author getByFirstNameAndLastName(String firstname, String lastname) {
        return null;
    }

    @Override
    public boolean update(Author author, int id) {
    if(authorRepository.existsById(id)) {
        author.setId(id);
        authorRepository.save(author);
        return  true;
    }
    return false;
    }

    @Override
    public boolean delete(int id) {
        if(authorRepository.existsById(id)) {
            authorRepository.deleteById(id);
        return true;
        }
        return false;
    }
}
