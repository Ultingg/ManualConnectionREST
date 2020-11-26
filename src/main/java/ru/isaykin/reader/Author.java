package ru.isaykin.reader;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.Email;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;


@Data
@Builder
@Table("authors")
@NoArgsConstructor
@AllArgsConstructor
public class Author implements Comparable<Author> {

    @Id
    private Long id;

    @Size(min = 3, message = "Minimal size of first name is 3 characters.")
    private String firstName;

    @Size(min = 3, message = "Minimal size of last name is 3 characters.")
    private String lastName;

    @Email(message = "Enter correct email address.")
    private String email;

    @Past(message = "Birth date cannot be in future.")
    private LocalDate birthdate;

    @Override
    public int compareTo(Author o) {
        return firstName.compareTo(o.firstName);
    }
}