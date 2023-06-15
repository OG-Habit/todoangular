package com.ivanwoogue.todo.controllers;

import com.ivanwoogue.todo.exceptions.ListContainsDeletedTodosException;
import com.ivanwoogue.todo.exceptions.TodoDescriptionNotFoundException;
import com.ivanwoogue.todo.exceptions.TodoNotFoundException;
import com.ivanwoogue.todo.models.Todo;
import com.ivanwoogue.todo.services.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:8000", "http://localhost:4200"})
@RequestMapping(value = "/todos", produces = MediaType.APPLICATION_JSON_VALUE)
public class TodoController {
    @Autowired
    private TodoService todoService;

    @GetMapping("")
    ResponseEntity<List<Todo>> getAllTodos() {
        try {
            List<Todo> allTodos = todoService.getAllTodos();
            return new ResponseEntity<>(allTodos, HttpStatus.OK);
        } catch(ListContainsDeletedTodosException e) {
            throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY, "List contains deleted todos", e);
        }
    }

    @PostMapping("/todo")
    ResponseEntity<Todo> addTodo(@RequestParam String description)  {
        try {
            Todo todo = todoService.addTodo(description);
            return new ResponseEntity<>(todo, HttpStatus.OK);
        } catch(TodoDescriptionNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Description for todo is missing.", e);
        }
    }

    @PutMapping("/todo")
    ResponseEntity<HttpStatus> updateTodo(@RequestBody Todo todo) {
        try {
            todoService.updateTodo(todo);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch(TodoNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo is not found.", e);
        }
    }
}