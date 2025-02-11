package dao;

import db.DataSource;
import entity.Author;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class AuthorCrudOperations implements CrudOperations<Author> {
    private final DataSource dataSource = new DataSource();
    Logger logger = Logger.getLogger("AuthorCrudOperations");

    @Override
    public List<Author> getAll(int page, int size, String orderBy) {
        if (page < 1) {
            throw new IllegalArgumentException("page must be greater than 0 but actual is " + page);
        }
        String sql = "SELECT a.id, a.name, a.birth_date FROM author a";
        if (orderBy != null && !orderBy.isEmpty()) {
            sql += " ORDER BY " + orderBy;
        }
        sql += " LIMIT ? OFFSET ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, size);
            preparedStatement.setInt(2, size * (page - 1));
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return mapAuthorFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Author> mapAuthorFromResultSet(ResultSet resultSet) throws SQLException {
        List<Author> authors = new ArrayList<>();
        while (resultSet.next()) {
            Author author = new Author();
            author.setId(resultSet.getString("id"));
            author.setName(resultSet.getString("name"));
            author.setBirthDate(resultSet.getDate("birth_date").toLocalDate());
            authors.add(author);
        }
        return authors;
    }

    @Override
    public List<Author> findByCriteria(List<Criteria> criteria) {
        List<Author> authors = new ArrayList<>();
        String sql = "select a.id, a.name, a.birth_date from author a where 1=1";
        for (Criteria c : criteria) {
            if ("name".equals(c.getColumn())) {
                sql += " and a." + c.getColumn() + " ilike '%" + c.getValue().toString() + "%'";
            } else if ("birth_date".equals(c.getColumn())) {
                sql += " or a." + c.getColumn() + " = '" + c.getValue().toString() + "'";
            }
        }
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            return mapAuthorFromResultSet(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Author findById(String id) {
        String sql = "select a.id, a.name, a.birth_date from author a where id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Author author = new Author();
                    author.setId(resultSet.getString("id"));
                    author.setName(resultSet.getString("name"));
                    author.setBirthDate(resultSet.getDate("birth_date").toLocalDate());
                    return author;
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Author> saveAll(List<Author> entities) {
        List<Author> savedAuthors = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            for (Author entityToSave : entities) {
                Author existingAuthor = findById(entityToSave.getId());
                if (existingAuthor != null) {
                    try (PreparedStatement statement = connection.prepareStatement(
                            "update author set name = ?, birth_date = ? where id = ?")) {
                        statement.setString(1, entityToSave.getName());
                        statement.setDate(2, Date.valueOf(entityToSave.getBirthDate()));
                        statement.setString(3, entityToSave.getId());
                        statement.executeUpdate();
                    }
                } else {
                    try (PreparedStatement statement = connection.prepareStatement(
                            "insert into author (id, name, birth_date) values (?, ?, ?)")) {
                        statement.setString(1, entityToSave.getId());
                        statement.setString(2, entityToSave.getName());
                        statement.setDate(3, Date.valueOf(entityToSave.getBirthDate()));
                        statement.executeUpdate();
                    }
                }
                savedAuthors.add(findById(entityToSave.getId()));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return savedAuthors;
    }
}
