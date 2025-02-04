package dao;

import java.util.List;

public interface CrudOperations<E> {
    List<E> getAll(int page, int size);

    List<E> findByCriteria(List<Criteria> criteria);

    E findById(String id);

    // Both create (if does not exist) or update (if exist) entities
    List<E> saveAll(List<E> entities);
}
