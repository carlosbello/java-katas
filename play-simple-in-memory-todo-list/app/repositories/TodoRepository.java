package repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.inject.Singleton;

import models.TodoItem;

import com.google.common.base.Strings;

@Singleton
public class TodoRepository {
	private final Map<String, TodoItem> todoList = new HashMap<>();

	public List<TodoItem> getAll() {
		return new ArrayList<>(todoList.values());
	}

	public TodoItem get(String id) {
		return todoList.get(id);
	}

	public TodoItem add(TodoItem todoItem) throws IllegalArgumentException {
		if (todoList.containsKey(todoItem.getId())) {
			throw new IllegalArgumentException(String.format("A task with id: %s already exists", todoItem.getId()));
		}
		if (Strings.isNullOrEmpty(todoItem.getId())) {
			todoItem.setId(UUID.randomUUID().toString());
		}
		todoList.put(todoItem.getId(), todoItem);
		return todoItem;
	}

	public TodoItem delete(String id) {
		return todoList.remove(id);
	}

	public TodoItem update(TodoItem todoItem) throws IllegalArgumentException {
		if (!todoList.containsKey(todoItem.getId())) {
			throw new IllegalArgumentException(String.format("No task with id: %s exists.", todoItem.getId()));
		}
		todoList.put(todoItem.getId(), todoItem);
		return todoItem;
	}
}
