package taskmanager;

import org.junit.jupiter.api.BeforeEach;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

	@BeforeEach
	public void init() {
		super.init(new InMemoryTaskManager());
	}

}