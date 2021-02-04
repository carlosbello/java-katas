package controllers.api.v1;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import models.TodoItem;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import play.test.WithApplication;
import repositories.TodoRepository;

import static org.junit.Assert.assertEquals;
import static play.inject.Bindings.bind;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.DELETE;
import static play.test.Helpers.GET;
import static play.test.Helpers.POST;
import static play.test.Helpers.PUT;
import static play.test.Helpers.route;

public class TodoListControllerTest extends WithApplication {
	private static final TodoItem ITEM1 = new TodoItem() {{
		setId("id1");
		setTitle("item1");
	}};
	private static final TodoItem ITEM2 = new TodoItem() {{
		setId("id2");
		setTitle("item2");
	}};
	private static final TodoItem ITEM3 = new TodoItem() {{
		setId("id3");
		setTitle("item3");
	}};

	private final TodoRepository repository = new TodoRepository();

	@Before
	public void setUp() throws Exception {
		repository.add(ITEM1);
		repository.add(ITEM2);
		repository.add(ITEM3);
	}

	@After
	public void cleanUp() {
		repository.getAll().forEach(todoItem -> repository.delete(todoItem.getId()));
	}

	@Override
	protected Application provideApplication() {
		return new GuiceApplicationBuilder()
				.overrides(bind(TodoRepository.class).toInstance(repository))
				.build();
	}

	@Test
	public void testGetAll() {
		Http.RequestBuilder request = new Http.RequestBuilder()
				.method(GET)
				.uri("/api/todolist");

		Result result = route(app, request);
		List<TodoItem> items = Json.fromJson(Json.parse(Helpers.contentAsString(result)), ArrayList.class);

		assertEquals(OK, result.status());
		assertEquals(3, items.size());
	}

	@Test
	public void testGetOne() {
		Http.RequestBuilder request = new Http.RequestBuilder()
				.method(GET)
				.uri("/api/todolist/" + ITEM2.getId());

		Result result = route(app, request);
		TodoItem item = Json.fromJson(Json.parse(Helpers.contentAsString(result)), TodoItem.class);

		assertEquals(OK, result.status());
		assertEquals(ITEM2.getId(), item.getId());
		assertEquals(ITEM2.getTitle(), item.getTitle());
	}

	@Test
	public void testDelete() {
		Http.RequestBuilder request = new Http.RequestBuilder()
				.method(DELETE)
				.uri("/api/todolist/" + ITEM1.getId());

		Result result = route(app, request);
		TodoItem deletedItem = Json.fromJson(Json.parse(Helpers.contentAsString(result)), TodoItem.class);

		assertEquals(OK, result.status());
		assertEquals(ITEM1.getId(), deletedItem.getId());
	}

	@Test
	public void testAdd() {
		var newItem = new TodoItem() {{
			setId("new-item");
			setTitle("new-title");
		}};
		Http.RequestBuilder request = new Http.RequestBuilder()
				.method(POST)
				.uri("/api/todolist")
				.bodyJson(Json.toJson(newItem));

		Result result = route(app, request);
		TodoItem addedItem = Json.fromJson(Json.parse(Helpers.contentAsString(result)), TodoItem.class);

		assertEquals(OK, result.status());
		assertEquals(newItem.getId(), addedItem.getId());
	}

	@Test
	public void testUpdate() {
		var newContent = new TodoItem() {{
			setTitle("updated-title");
		}};
		Http.RequestBuilder request = new Http.RequestBuilder()
				.method(PUT)
				.uri("/api/todolist/" + ITEM3.getId())
				.bodyJson(Json.toJson(newContent));

		Result result = route(app, request);
		TodoItem updatedItem = Json.fromJson(Json.parse(Helpers.contentAsString(result)), TodoItem.class);

		assertEquals(OK, result.status());
		assertEquals(ITEM3.getId(), updatedItem.getId());
		assertEquals(newContent.getTitle(), updatedItem.getTitle());
	}

}
