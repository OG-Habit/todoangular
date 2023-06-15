package com.ivanwoogue.todo.repositories;

import com.ivanwoogue.todo.models.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findAllByIsDeletedIsFalse();
}
