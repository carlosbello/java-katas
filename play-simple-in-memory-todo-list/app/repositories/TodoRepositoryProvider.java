package repositories;

import javax.inject.Provider;

public class TodoRepositoryProvider implements Provider<TodoRepository> {
	@Override
	public TodoRepository get() {
		return TodoRepository.getInstance();
	}
}
