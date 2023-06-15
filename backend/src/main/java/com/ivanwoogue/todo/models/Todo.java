package com.ivanwoogue.todo.models;

import net.bytebuddy.implementation.bind.annotation.Default;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "todo")
public class Todo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;
    @Column(nullable = false)
    private String todoDescription;
    @Column(nullable = false)
    private boolean isFinished = false;
    @Column(nullable = false)
    private boolean isDeleted = false;

    public Todo() {}

    public Todo(String todoDescription) {
        this.todoDescription = todoDescription;
    }

    public Todo(Long id, String todoDescription) {
        this.id = id;
        this.todoDescription = todoDescription;
    }

    public Todo(Long id, String todoDescription, boolean isDeleted) {
        this.id = id;
        this.todoDescription = todoDescription;
        this.isDeleted = isDeleted;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTodoDescription() {
        return todoDescription;
    }

    public void setTodoDescription(String todoDescription) {
        this.todoDescription = todoDescription;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}