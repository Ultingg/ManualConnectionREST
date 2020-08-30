package ru.isaykin.services;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.isaykin.reader.Author;

import javax.transaction.Transactional;

@Transactional
public interface AuthorRepository extends JpaRepository<Author,Integer> {

}
