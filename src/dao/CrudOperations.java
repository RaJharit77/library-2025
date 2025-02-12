package dao;

import java.util.List;

public interface CrudOperations<E> {
    List<E> getAll(int page, int size, String orderBy);

    List<E> findByCriteria(List<Criteria> criteria, int page, int pageSize, String orderBy);

    E findById(String id);

    // Both create (if does not exist) or update (if exist) entities
    List<E> saveAll(List<E> entities);
}
