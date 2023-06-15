package com.ivanwoogue.todo.controllers;

import com.ivanwoogue.todo.exceptions.ListContainsDeletedTodosException;
import com.ivanwoogue.todo.exceptions.TodoDescriptionNotFoundException;
import com.ivanwoogue.todo.exceptions.TodoNotFoundException;
import com.ivanwoogue.todo.models.Todo;
import com.ivanwoogue.todo.services.TodoService;
import org.apache.coyote.Response;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TodoControllerTest {
    @Mock
    TodoService todoService;
    @InjectMocks
    TodoController todoController;
    static List<Todo> todoList;
    static Todo todo;

    @BeforeAll
    static void setUp() {
        todo = new Todo(1L, "Todo description");
        todoList = new ArrayList<>();
        todoList.add(todo);
        todoList.add(new Todo(2L, "Deleted todo."));
    }

    @Test
    void shouldSendListOfTodosAndHttpStatusOk() {
        ResponseEntity<List<Todo>> response = new ResponseEntity<>(todoList, HttpStatus.OK);
        when(todoService.getAllTodos())
                .thenReturn(todoList);

        assertThat(todoController.getAllTodos())
                .isEqualTo(response);
    }

    @Test
    void shouldRaiseResponseStatusExceptionForHavingDeletedTodos() {
        when(todoService.getAllTodos())
                .thenThrow(ListContainsDeletedTodosException.class);

        assertThatThrownBy(() -> todoController.getAllTodos())
                .isInstanceOf(ResponseStatusException.class)
                .hasCauseInstanceOf(ListContainsDeletedTodosException.class)
                .hasMessageContaining("List contains deleted todos");
    }

    @Test
    void shouldSendHttpStatusCreatedForAddingTodo() {
        ResponseEntity<HttpStatus> response = new ResponseEntity(HttpStatus.CREATED);
        assertThat(todoController.addTodo(anyString()))
                .isEqualTo(response);
    }

    @Test
    void shouldRaiseResponseStatusExceptionForHavingEmptyDescription() {
        when(todoService.addTodo(""))
                .thenThrow(TodoDescriptionNotFoundException.class);

        assertThatThrownBy(() -> todoController.addTodo(""))
                .isInstanceOf(ResponseStatusException.class)
                .hasCauseInstanceOf(TodoDescriptionNotFoundException.class)
                .hasMessageContaining("Description for todo is missing.");
    }

    @Test
    void shouldSendHttpOkCreatedForUpdatingTodo() {
        ResponseEntity<HttpStatus> response = new ResponseEntity(HttpStatus.OK);
        assertThat(todoController.updateTodo(todo))
                .isEqualTo(response);
    }

    @Test
    void shouldRaiseResponseStatusExceptionForMissingTodo() {
        when(todoService.updateTodo(todo))
                .thenThrow(TodoNotFoundException.class);

        assertThatThrownBy(() -> todoController.updateTodo(todo))
                .isInstanceOf(ResponseStatusException.class)
                .hasCauseInstanceOf(TodoNotFoundException.class)
                .hasMessageContaining("Todo is not found.");
    }
}