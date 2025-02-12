package test;

import dao.AuthorCrudOperations;
import dao.Criteria;
import entity.Author;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;

class AuthorCrudOperationsTest {
    // Always rename the class to test to 'subject'
    AuthorCrudOperations subject = new AuthorCrudOperations();

    @Test
    void read_all_authors_ok() {
        // Test for data and potential mock
        Author expectedAuthor = authorJJR();

        // Subject and the function to test
        List<Author> actual = subject.getAll(1, 3, "name");

        // Assertions : verification to be made automatically
        assertTrue(actual.contains(expectedAuthor));
    }

    @Test
    void read_author_by_id_ok() {
        Author expectedAuthor = authorJJR();

        Author actual = subject.findById(expectedAuthor.getId());

        assertEquals(expectedAuthor, actual);
    }

    @Test
    void create_then_update_author_ok() {
        var newAuthor = newAuthor(randomUUID().toString(), "Random famous author", LocalDate.of(2000, 1, 1));
        var savedAuthors = subject.saveAll(List.of(newAuthor));

        assertEquals(1, savedAuthors.size());
        assertEquals(newAuthor, savedAuthors.get(0));

        newAuthor.setName("Updated famous author");
        newAuthor.setBirthDate(LocalDate.of(1990, 1, 1));
        var updatedAuthors = subject.saveAll(List.of(newAuthor));

        assertEquals(1, updatedAuthors.size());
        assertEquals(newAuthor, updatedAuthors.get(0));

        var existingAuthors = subject.getAll(1, 3, "name");
        assertTrue(existingAuthors.containsAll(updatedAuthors));
    }

    // TODO : make the changes inside the CrudOperations and its implementation to handle this
    // Once test passed, set UnitTest corresponding
    @Test
    void read_authors_filter_by_name_or_birthday_between_intervals() {
        ArrayList<Criteria> criteria = new ArrayList<>();
        criteria.add(new Criteria("name", "rado"));
        criteria.add(new Criteria("birth_date", LocalDate.of(2000, 1, 1)));
        List<Author> expected = List.of(
                authorJJR(),
                authorRado());

        List<Author> actual = subject.findByCriteria(criteria, 1, 2, "name");

        assertEquals(expected, actual);
        assertTrue(actual.stream()
                .allMatch(author -> author.getName().toLowerCase().contains("rado")
                || author.getBirthDate().equals(LocalDate.of(2000, 1, 1))));

    }

    private Author authorRado() {
        return newAuthor("author2_id", "Rado", LocalDate.of(1990, 1, 1));
    }

    // TODO : make the changes inside the CrudOperations and its implementation to handle this
    // Once test passed, set UnitTest corresponding
    @Test
    void read_authors_order_by_name_or_birthday_or_both() {
        List<Author> authorsOrderedByName = subject.getAll(1, 3, "name");
        List<Author> authorsOrderedByBirthDate = subject.getAll(1, 3, "birth_date");
        List<Author> authorsOrderedByNameAndBirthDate = subject.getAll(1, 3, "name, birth_date");

        assertTrue(isSorted(authorsOrderedByName, Comparator.comparing(Author::getName)));
        assertTrue(isSorted(authorsOrderedByBirthDate, Comparator.comparing(Author::getBirthDate)));
        assertTrue(isSorted(authorsOrderedByNameAndBirthDate, Comparator.comparing(Author::getName).thenComparing(Author::getBirthDate)));
    }

    @Test
    void read_filtered_ordered_and_paginated() {
        ArrayList<Criteria> criteria = new ArrayList<>();
        criteria.add(new Criteria("name", "rado"));
        criteria.add(new Criteria("birth_date", LocalDate.of(2000, 1, 1)));

        List<Author> actual = subject.findByCriteria(criteria, 1, 2, "name");

        assertEquals(2, actual.size());
        assertTrue(actual.stream()
                .allMatch(author -> author.getName().toLowerCase().contains("rado")
                        || author.getBirthDate().equals(LocalDate.of(2000, 1, 1))));
        assertTrue(isSorted(actual, Comparator.comparing(Author::getName)));
    }

    private boolean isSorted(List<Author> authors, Comparator<Author> comparator) {
        for (int i = 0; i < authors.size() - 1; i++) {
            if (comparator.compare(authors.get(i), authors.get(i + 1)) > 0) {
                return false;
            }
        }
        return true;
    }

    private Author authorJJR() {
        Author expectedAuthor = new Author();
        expectedAuthor.setId("author1_id");
        expectedAuthor.setName("JJR");
        expectedAuthor.setBirthDate(LocalDate.of(2000, 1, 1));
        return expectedAuthor;
    }

    private Author newAuthor(String id, String name, LocalDate birthDate) {
        Author author = new Author();
        author.setId(id);
        author.setName(name);
        author.setBirthDate(birthDate);
        return author;
    }
}

