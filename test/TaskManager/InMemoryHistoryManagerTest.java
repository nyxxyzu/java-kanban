package TaskManager;

import Tasks.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
	HistoryManager historyManager = new InMemoryHistoryManager();

	@Test
	public void tasksAddedToHistoryManagerKeepTheirOriginalData() {
		Task task = new Task("Имя", "Описание", Status.NEW, 1);
		Task savedTask = task;
		historyManager.add(task);
		task = new Task("Имя1","Описание1", Status.DONE, 1);
		assertEquals(savedTask.getName(), historyManager.getHistory().get(0).getName());
		assertEquals(savedTask.getDescription(), historyManager.getHistory().get(0).getDescription());
		assertEquals(savedTask.getStatus(), historyManager.getHistory().get(0).getStatus());
		assertEquals(savedTask.getId(), historyManager.getHistory().get(0).getId());

	}

}