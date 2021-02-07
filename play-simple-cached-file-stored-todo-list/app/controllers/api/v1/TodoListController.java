package controllers.api.v1;

import controllers.exceptions.InvalidArgumentException;
import controllers.models.TodoItem;
import controllers.repositories.TodoListRepository;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class TodoListController extends Controller {
    private final TodoListRepository repository;
    private final HttpExecutionContext ec;

    @Inject
    public TodoListController(TodoListRepository repository, HttpExecutionContext ec) {
        this.repository = repository;
        this.ec = ec;
    }

    public CompletionStage<Result> getAll() {
        return CompletableFuture.supplyAsync(
                () -> {
                    List<TodoItem> todoList = null;
                    try {
                        todoList = repository.getAll();
                        return ok(Json.toJson(todoList));
                    } catch (IOException e) {
                        return internalServerError(e.getMessage());
                    }

                },
                ec.current()
        );
    }

    public CompletionStage<Result> get(String id) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        var todoItem = repository.get(id);
                        return ok(Json.toJson(todoItem));
                    } catch (IOException e) {
                        return internalServerError(e.getMessage());
                    }
                },
                ec.current()
        );
    }

    public CompletionStage<Result> add(Http.Request request) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        var todoItem = request.body()
                                .parseJson(TodoItem.class)
                                .orElseThrow(IllegalArgumentException::new);
                        var addedItem = repository.add(todoItem);
                        return ok(Json.toJson(addedItem));
                    } catch (IllegalArgumentException e) {
                        return badRequest("Invalid request: a JSON todo item was expected.");
                    } catch (InvalidArgumentException e) {
                        return badRequest(e.getMessage());
                    } catch (IOException e) {
                        return internalServerError(e.getMessage());
                    }
                },
                ec.current()
        );
    }

    public CompletionStage<Result> update(String id, Http.Request request) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        var todoItem = request.body()
                                .parseJson(TodoItem.class)
                                .orElseThrow(IllegalArgumentException::new);
                        todoItem.setId(id);
                        var addedItem = repository.update(todoItem);
                        return ok(Json.toJson(addedItem));
                    } catch (IllegalArgumentException e) {
                        return badRequest("Invalid request: a JSON todo item was expected.");
                    } catch (InvalidArgumentException e) {
                        return badRequest(e.getMessage());
                    } catch (IOException e) {
                        return internalServerError(e.getMessage());
                    }
                },
                ec.current()
        );
    }

    public CompletionStage<Result> delete(String id) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        var deletedItem = repository.delete(id);
                        return ok(Json.toJson(deletedItem));
                    } catch (IOException e) {
                        return internalServerError(e.getMessage());
                    }
                },
                ec.current()
        );
    }
}
