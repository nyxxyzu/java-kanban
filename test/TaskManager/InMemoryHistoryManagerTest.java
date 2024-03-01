package TaskManager;

import Tasks.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
	HistoryManager historyManager = new InMemoryHistoryManager();
	TaskManager manager = new InMemoryTaskManager();

	@Test
	public void tasksAddedToHistoryManagerKeepTheirOriginalData() {
		Task task = new Task("Имя", "Описание", Status.NEW, 1);
		Task savedTask = task;
		historyManager.add(task);
		task = new Task("Имя1","Описание1", Status.DONE, 1);
		assertEquals(savedTask.getName(), historyManager.getHistory().getFirst().getName());
		assertEquals(savedTask.getDescription(), historyManager.getHistory().getFirst().getDescription());
		assertEquals(savedTask.getStatus(), historyManager.getHistory().getFirst().getStatus());
		assertEquals(savedTask.getId(), historyManager.getHistory().getFirst().getId());

	}

}