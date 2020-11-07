package ru.isaykin.reader;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;


@Data
@Setter
@Getter
@Table("authors")
@NoArgsConstructor
public class Author implements Comparable<Author> {

    @Id
    @NonNull
    private Long id;

    @NonNull
    private String firstName;

    @NonNull
    private String lastName;

    @NonNull
    private String email;


    @NonNull
    private LocalDate birthdate;

    @Override
    public int compareTo(Author o) {
        return firstName.compareTo(o.firstName);
    }
}