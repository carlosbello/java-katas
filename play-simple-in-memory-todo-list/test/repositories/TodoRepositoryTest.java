package repositories;

import org.junit.After;
import org.junit.Test;

import models.TodoItem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TodoRepositoryTest {
	private final TodoRepository repository = new TodoRepository();

	@After
	public void cleanUp() {
		repository.getAll().forEach(todoItem -> repository.delete(todoItem.getId()));
	}

	@Test
	public void getAllReturnsEmptyListWhenNoElementHasBeenAdded() {
		assertTrue(repository.getAll().isEmpty());
	}

	@Test
	public void getAllReturnsEmptyListWhenAllElementHasBeenDeleted() throws IllegalArgumentException {
		repository.add(new TodoItem() {{
			setId("id1");
		}});
		repository.add(new TodoItem() {{
			setId("id2");
		}});
		repository.delete("id1");
		repository.delete("id2");

		assertTrue(repository.getAll().isEmpty());
	}

	@Test
	public void getReturnsExistingElement() throws IllegalArgumentException {
		var todoItem = new TodoItem() {{
			setId("id1");
		}};
		repository.add(todoItem);

		assertEquals(repository.get("id1"), todoItem);
	}

	@Test(expected = IllegalArgumentException.class)
	public void addThowsExceptionIfNewElementIdExistsInTheRepository() throws IllegalArgumentException {
		TodoItem newTodoItem = new TodoItem() {{
			setId("id1");
		}};

		repository.add(newTodoItem);
		repository.add(newTodoItem);
	}

	@Test
	public void add() throws IllegalArgumentException {
		TodoItem newTodoItem = new TodoItem() {{
			setId("id1");
		}};

		assertTrue(repository.getAll().isEmpty());

		var addedItem = repository.add(newTodoItem);

		assertEquals(addedItem, newTodoItem);
	}

	@Test
	public void delete() throws IllegalArgumentException {
		repository.add(new TodoItem() {{
			setId("id1");
		}});

		assertNotNull(repository.get("id1"));

		repository.delete("id1");

		assertNull(repository.get("id1"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void updateThrowsExceptionWhenElementToUpdateDoesNotExist() throws IllegalArgumentException {
		repository.update(new TodoItem() {{
			setId("non-existing-element");
		}});
	}

	@Test
	public void updateChangeExistingElement() throws IllegalArgumentException {
		var originalTodoItem = new TodoItem() {{
			setId("id1");
			setTitle("Original title");
		}};
		var updatedTodoItem = new TodoItem() {{
			setId("id1");
			setTitle("Updated title");
		}};

		repository.add(originalTodoItem);
		assertEquals(1, repository.getAll().size());

		repository.update(updatedTodoItem);
		assertEquals(1, repository.getAll().size());

		var currentTodoItem = repository.get("id1");

		assertEquals(updatedTodoItem.getTitle(), currentTodoItem.getTitle());
		assertNotEquals(originalTodoItem.getTitle(), currentTodoItem.getTitle());
	}
}