package test;

import dao.AuthorCrudOperations;
import dao.Criteria;
import entity.Author;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
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
        List<Author> actual = subject.getAll(1, 3);

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
        var authors = newAuthor(randomUUID().toString(), "Random famous author", LocalDate.of(2000, 1, 1));

        var actual = subject.saveAll(List.of(authors));
        //TODO: update created authors with saveAll when saveAll handle update

        var existingAuthors = subject.getAll(1, 3);
        assertEquals(List.of(authors), actual);
        assertTrue(existingAuthors.containsAll(actual));
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

        List<Author> actual = subject.findByCriteria(criteria);

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
        assertThrows(UnsupportedOperationException.class, () -> {
            throw new UnsupportedOperationException("Not implemented yet");
        });
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

