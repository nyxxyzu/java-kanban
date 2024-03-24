package taskmanager;

import tasks.Task;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

	@Test
	public void managerCreatorReturnsWorkingManagers() {
		HistoryManager historyManager = Managers.getDefaultHistory();
		TaskManager taskManager = Managers.getDefault();
		Task task = new Task("Test addNewTask", "Test addNewTask description",Status.NEW);
		int taskId = taskManager.createTask(task).getId();
		Task getTask = taskManager.getTask(taskId);
		historyManager.add(task);
		final List<Task> history = historyManager.getHistory();
		assertNotNull(historyManager);
		assertNotNull(taskManager);
		assertNotNull(history);
		assertEquals(1, history.size());
		assertNotNull(getTask);

	}


}