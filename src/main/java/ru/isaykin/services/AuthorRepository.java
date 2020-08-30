package ru.isaykin.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.isaykin.reader.Author;

public interface AuthorRepository extends JpaRepository<Author,Integer>, JpaSpecificationExecutor<Author> { }
