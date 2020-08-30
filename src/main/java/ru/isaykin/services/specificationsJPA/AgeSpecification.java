package ru.isaykin.services.specificationsJPA;

import org.springframework.data.jpa.domain.Specification;
import ru.isaykin.reader.Author;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class AgeSpecification {


    public static Specification<Author> getByFirstName(String firstName){
        return new Specification<Author>() {
            @Override
            public Predicate toPredicate(Root<Author> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("first_name"), firstName);
            }
        };
    }
}
