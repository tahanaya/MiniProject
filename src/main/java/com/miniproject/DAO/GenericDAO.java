package com.miniproject.DAO;

import java.sql.SQLException;
import java.util.List;

public interface GenericDAO<T> {
    T findById(int id);
    List<T> findAll();
    void save(T entity);
    void update(T entity)throws SQLException;
    void delete(int id);
}