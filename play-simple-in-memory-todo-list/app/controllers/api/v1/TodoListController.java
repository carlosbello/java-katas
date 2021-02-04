package controllers.api.v1;

import javax.inject.Inject;

import models.TodoItem;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.Request;
import play.mvc.Result;
import play.mvc.Results;
import repositories.TodoRepository;

public class TodoListController extends Controller {

	private final TodoRepository todoRepository;

	@Inject
	public TodoListController(TodoRepository todoRepository) {
		this.todoRepository = todoRepository;
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
		} catch (IllegalArgumentException e) {
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
		} catch (IllegalArgumentException e) {
			return Results.badRequest(e.getMessage());
		}
	}
}
