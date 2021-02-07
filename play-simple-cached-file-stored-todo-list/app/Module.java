import com.google.inject.AbstractModule;
import controllers.repositories.TodoListCachedRepository;
import controllers.repositories.TodoListRepository;

public class Module extends AbstractModule {
    protected void configure() {
        bind(TodoListRepository.class).to(TodoListCachedRepository.class);
    }
}
