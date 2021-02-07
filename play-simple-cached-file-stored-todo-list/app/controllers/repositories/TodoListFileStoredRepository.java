package controllers.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.typesafe.config.Config;
import controllers.exceptions.InvalidArgumentException;
import controllers.models.TodoItem;
import play.libs.Json;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@Singleton
public class TodoListFileStoredRepository implements TodoListRepository {
    private final String fileStorageName;
    private final TypeReference<Map<String, TodoItem>> TODO_LIST_TYPE = new TypeReference<>() {
    };
    private final ObjectMapper objectMapper;

    @Inject
    public TodoListFileStoredRepository(Config config, ObjectMapper objectMapper) {
        fileStorageName = config.getString("todo.storage");
        this.objectMapper = objectMapper;
    }

    @Override
    public List<TodoItem> getAll() throws IOException {
        try {
            Map<String, TodoItem> indexedTodoList = loadFromFile();
            return new ArrayList<>(indexedTodoList.values());
        } catch (FileNotFoundException e) {
            Map<String, TodoItem> newEmptyTodoList = Collections.emptyMap();
            saveToFile(newEmptyTodoList);
            return List.of();
        }
    }

    @Override
    public TodoItem get(String id) throws IOException {
        var indexedTodoList = loadFromFile();
        return indexedTodoList.get(id);
    }

    @Override
    public TodoItem add(TodoItem todoItem) throws IOException, InvalidArgumentException {
        if (Strings.isNullOrEmpty(todoItem.getId())) {
            todoItem.setId(UUID.randomUUID().toString());
        }
        var indexedTodoList = loadFromFile();

        if (indexedTodoList.containsKey(todoItem.getId())) {
            throw new InvalidArgumentException(String.format("A task with id '%s' already exists.", todoItem.getId()));
        }
        indexedTodoList.put(todoItem.getId(), todoItem);
        saveToFile(indexedTodoList);

        return todoItem;
    }

    @Override
    public TodoItem update(TodoItem todoItem) throws IOException, InvalidArgumentException {
        var indexedTodoList = loadFromFile();

        if (!indexedTodoList.containsKey(todoItem.getId())) {
            throw new InvalidArgumentException(String.format("No task with id '%s' can be found.", todoItem.getId()));
        }
        indexedTodoList.put(todoItem.getId(), todoItem);
        saveToFile(indexedTodoList);

        return todoItem;
    }

    @Override
    public TodoItem delete(String id) throws IOException {
        var indexedTodoList = loadFromFile();
        var deletedItem = indexedTodoList.remove(id);
        saveToFile(indexedTodoList);
        return deletedItem;
    }

    private void saveToFile(Map<String, TodoItem> indexedTodoList) throws IOException {
        var fileWriter = new FileWriter(fileStorageName);
        fileWriter.write(Json.stringify(Json.toJson(indexedTodoList)));
        fileWriter.close();
    }

    private Map<String, TodoItem> loadFromFile() throws IOException {
        FileInputStream todoListFile = new FileInputStream(fileStorageName);
        return objectMapper.readValue(todoListFile, TODO_LIST_TYPE);
    }
}
