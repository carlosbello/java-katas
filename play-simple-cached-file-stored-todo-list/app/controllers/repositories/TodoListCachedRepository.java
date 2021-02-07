package controllers.repositories;

import com.typesafe.config.Config;
import controllers.exceptions.InvalidArgumentException;
import controllers.models.TodoItem;
import play.cache.SyncCacheApi;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

public class TodoListCachedRepository implements TodoListRepository {
    private final TodoListFileStoredRepository fileStoredRepository;
    private final SyncCacheApi cache;
    private final int cacheReadExpirationInSeconds;

    @Inject
    public TodoListCachedRepository(TodoListFileStoredRepository fileStoredRepository, SyncCacheApi cache, Config config) {
        this.fileStoredRepository = fileStoredRepository;
        this.cache = cache;
        this.cacheReadExpirationInSeconds = config.getInt("todo.cache.readExpirationInSeconds");
    }

    @Override
    public List<TodoItem> getAll() throws IOException {
        return cache.getOrElseUpdate("getAll", fileStoredRepository::getAll, cacheReadExpirationInSeconds);
    }

    @Override
    public TodoItem get(String id) throws IOException {
        return cache.getOrElseUpdate("get:" + id, () -> fileStoredRepository.get(id), cacheReadExpirationInSeconds);
    }

    @Override
    public TodoItem add(TodoItem todoItem) throws IOException, InvalidArgumentException {
        var newItem = fileStoredRepository.add(todoItem);
        cache.remove("getAll");
        return newItem;
    }

    @Override
    public TodoItem update(TodoItem todoItem) throws IOException, InvalidArgumentException {
        var updatedItem = fileStoredRepository.update(todoItem);
        // Intentionally only change the cache related to the updated item,
        // leaving the "getAll" cache untouched to see the difference in the responses
        cache.remove("get:" + updatedItem.getId());
        return updatedItem;
    }

    @Override
    public TodoItem delete(String id) throws IOException {
        // Intentionally leave both reading cache untouched to see how the deleted element only
        // disappears after the cache timeout
        return fileStoredRepository.delete(id);
    }
}
