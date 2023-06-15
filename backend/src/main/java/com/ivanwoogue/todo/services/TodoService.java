package com.ivanwoogue.todo.services;

import com.ivanwoogue.todo.exceptions.ListContainsDeletedTodosException;
import com.ivanwoogue.todo.exceptions.TodoDescriptionNotFoundException;
import com.ivanwoogue.todo.exceptions.TodoNotFoundException;
import com.ivanwoogue.todo.models.Todo;
import com.ivanwoogue.todo.repositories.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {
    @Autowired
    private TodoRepository todoRepository;

    public List<Todo> getAllTodos() {
        List<Todo> results = todoRepository.findAllByIsDeletedIsFalse();
        for (Todo todo : results) {
            if(todo.isDeleted()) {
                throw new ListContainsDeletedTodosException();
            }
        }
        return results;
    }

    public Todo addTodo(String description) {
        Todo todo = new Todo(description);
        if(!todo.getTodoDescription().isEmpty()) {
            return todoRepository.save(todo);
        } else {
            throw new TodoDescriptionNotFoundException();
        }
    }

    public Todo updateTodo(Todo todo) {
        Todo todoFromDB = todoRepository.getReferenceById(todo.getId());
        if(todoFromDB != null) {
            return todoRepository.save(todo);
        } else {
            throw new TodoNotFoundException();
        }
    }
}
