package controllers.api.v1;

import javax.inject.Inject;

import models.TodoItem;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.Request;
import play.mvc.Result;
import play.mvc.Results;
import repositories.TodoRepository;
import repositories.TodoRepositoryProvider;

public class TodoListController extends Controller {

	private final TodoRepository todoRepository;

	@Inject
	public TodoListController(TodoRepositoryProvider todoRepositoryProvider) {
		this.todoRepository = todoRepositoryProvider.get();
	}

	public Result getAll() {
		return ok(Json.toJson(todoRepository.getAll()));
	}

	public Result get(String id) {
		return ok(Json.toJson(todoRepository.get(id)));
	}

	public Result add(Request request) {
		var newTodoItem = Json.fromJson(request.body().asJson(), TodoItem.class);
		try {
			return ok(Json.toJson(todoRepository.add(newTodoItem)));
		} catch (Exception e) {
			return Results.badRequest(e.getMessage());
		}
	}

	public Result delete(String id) {
		return ok(Json.toJson(todoRepository.delete(id)));
	}

	public Result update(Request request, String id) {
		var todoItem = Json.fromJson(request.body().asJson(), TodoItem.class);
		todoItem.setId(id);
		try {
			return ok(Json.toJson(todoRepository.update(todoItem)));
		} catch (Exception e) {
			return Results.badRequest(e.getMessage());
		}
	}
}
