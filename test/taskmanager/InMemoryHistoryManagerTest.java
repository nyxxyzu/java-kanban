package taskmanager;

import tasks.Status;
import tasks.Task;
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

	@Test
	public void historyManagerAddAndRemoveWorkCorrectly() {
		Task task1 = new Task("Имя1", "Описание1", Status.NEW, 1);
		Task task2 = new Task("Имя2", "Описание2", Status.NEW, 2);
		Task task3 = new Task("Имя3", "Описание3", Status.NEW, 1);
		historyManager.add(task1);
		historyManager.add(task2);
		historyManager.add(task3);
		assertEquals(2,historyManager.getHistory().size());
		assertEquals(historyManager.getHistory().get(0), task2);
		historyManager.remove(1);
		assertEquals(1,historyManager.getHistory().size());
		assertEquals(historyManager.getHistory().get(0), task2);

	}
}