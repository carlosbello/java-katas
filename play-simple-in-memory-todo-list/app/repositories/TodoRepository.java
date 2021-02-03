package repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import models.TodoItem;

import com.google.common.base.Strings;

public class TodoRepository {
	private static TodoRepository instance;

	private final Map<String, TodoItem> todoList = new HashMap<>();

	private TodoRepository() {
	}

	public static TodoRepository getInstance() {
		if (instance == null) {
			instance = new TodoRepository();
		}
		return instance;
	}

	public List<TodoItem> getAll() {
		return new ArrayList<>(todoList.values());
	}

	public TodoItem get(String id) {
		return todoList.get(id);
	}

	public TodoItem add(TodoItem todoItem) throws Exception {
		if (todoList.containsKey(todoItem.getId())) {
			throw new Exception(String.format("A task with id: %s already exists", todoItem.getId()));
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

	public TodoItem update(TodoItem todoItem) throws Exception {
		if (!todoList.containsKey(todoItem.getId())) {
			throw new Exception(String.format("No task with id: %s exists.", todoItem.getId()));
		}
		todoList.put(todoItem.getId(), todoItem);
		return todoItem;
	}
}
