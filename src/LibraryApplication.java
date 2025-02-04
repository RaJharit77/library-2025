import dao.AuthorCrudOperations;
import db.DataSource;
import entity.Author;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class LibraryApplication {
    static Logger logger = Logger.getLogger(LibraryApplication.class.getName());

    public static void main(String[] args) throws SQLException {
        // Check database connection works properly !
        DataSource dataSource = new DataSource();
        Connection connection = dataSource.getConnection();
        logger.info(connection.toString());
        connection.close();

        // Check author getAll() works
        AuthorCrudOperations authorCrudOperations = new AuthorCrudOperations();
        List<Author> authors = authorCrudOperations.getAll(1, 30);
        logger.info(authors.toString());
    }
}
