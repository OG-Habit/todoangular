package com.ivanwoogue.todo.services;

import com.ivanwoogue.todo.exceptions.ListContainsDeletedTodosException;
import com.ivanwoogue.todo.exceptions.TodoDescriptionNotFoundException;
import com.ivanwoogue.todo.exceptions.TodoNotFoundException;
import com.ivanwoogue.todo.models.Todo;
import com.ivanwoogue.todo.repositories.TodoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TodoServiceTest {
    @Mock
    private TodoRepository todoRepository;
    @InjectMocks
    private TodoService todoService;
    @Captor
    ArgumentCaptor<Todo> todoCaptor;

    static List<Todo> dummyTodoList;
    static Todo dummyTodo1, dummyTodo2;
    static Todo updatedTodo1;
    static Todo notFoundTodo;
    static Todo dummyTodoMissingDescription;
    static String DUMMY_DESCRIPTION;

    @BeforeAll
    static void beforeAll() {
        dummyTodoList = new ArrayList<>();
        dummyTodo1 = new Todo(1L, "Do task 1");
        dummyTodo2 = new Todo(2L, "Do task 2");

        DUMMY_DESCRIPTION = "Description for todo";
        updatedTodo1 = new Todo(1L, "Updated task here");
        notFoundTodo = new Todo(4L, "Todo not found in db.");
        dummyTodoMissingDescription = new Todo(5L, "");

        dummyTodoList.add(dummyTodo1);
        dummyTodoList.add(dummyTodo2);
    }

    @Test
    void shouldGetAllTodos() {
        List<Todo> list;
        when(todoRepository.findAllByIsDeletedIsFalse())
                .thenReturn(dummyTodoList);

        list = todoService.getAllTodos();

        verify(todoRepository)
                .findAllByIsDeletedIsFalse();
        assertThat(list).isEqualTo(dummyTodoList);
    }

    @Test
    void shouldThrowListContainsDeletedTodosException() {
        List<Todo> wrongList = new ArrayList<>();
        wrongList.add(dummyTodo1);
        wrongList.add(new Todo(4L, "Wrong list", true));
        wrongList.add(dummyTodo2);

        when(todoRepository.findAllByIsDeletedIsFalse())
                .thenReturn(wrongList);

        assertThatThrownBy(() -> todoService.getAllTodos())
                .isInstanceOf(ListContainsDeletedTodosException.class);
    }

    @Test
    void shouldAddTodo() {
        todoService.addTodo(DUMMY_DESCRIPTION);
        verify(todoRepository).save(todoCaptor.capture());
        assertThat(todoCaptor.getValue().getTodoDescription())
                .isEqualTo(DUMMY_DESCRIPTION);
    }

    @Test
    void shouldThrowTodoDescriptionNotFoundException() {
        assertThatThrownBy(() -> todoService.addTodo(""))
                .isInstanceOf(TodoDescriptionNotFoundException.class);
    }

    @Test
    void shouldUpdateTodo() {
        when(todoRepository.getReferenceById(1L))
                .thenReturn(dummyTodo1);

        todoService.updateTodo(updatedTodo1);

        verify(todoRepository).save(todoCaptor.capture());
        assertThat(todoCaptor.getValue().getTodoDescription())
                .isEqualTo(updatedTodo1.getTodoDescription());
    }

    @Test
    void shouldThrowTodoNotFoundExceptionWhenUpdating() {
        when(todoRepository.getReferenceById(notFoundTodo.getId()))
                .thenReturn(null);

        assertThatThrownBy(() -> todoService.updateTodo(notFoundTodo))
                .isInstanceOf(TodoNotFoundException.class);
    }
}