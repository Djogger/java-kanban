package manager;

import task.Task;

public class sprint8TestInMemoryTaskManager extends TaskManagerTest<InMemoryTaskManager<Task>> {
    @Override
    protected InMemoryTaskManager<Task> createTaskManager() {
        return new InMemoryTaskManager<>();
    }
}
