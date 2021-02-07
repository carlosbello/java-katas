package controllers.repositories;

import controllers.exceptions.InvalidArgumentException;
import controllers.models.TodoItem;

import java.io.IOException;
import java.util.List;

public interface TodoListRepository {
    List<TodoItem> getAll() throws IOException;

    TodoItem get(String id) throws IOException;

    TodoItem add(TodoItem todoItem) throws IOException, InvalidArgumentException;

    TodoItem update(TodoItem todoItem) throws IOException, InvalidArgumentException;

    TodoItem delete(String id) throws IOException;
}
