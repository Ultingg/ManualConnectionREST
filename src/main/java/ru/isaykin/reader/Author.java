package ru.isaykin.reader;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;


@NoArgsConstructor
@Data


public class Author implements Comparable<Author> {

    @NonNull
    private int id;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;

    @NonNull
    private String email;
    @NonNull
    private LocalDate birthDate;

    @Override
    public int compareTo(Author o) {
        return firstName.compareTo(o.firstName);
    }


}