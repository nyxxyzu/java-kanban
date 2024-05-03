package taskmanager;

import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

	T manager;

	void init(T taskManager) {
		this.manager = taskManager;
	}

	@Test
	public void taskManagerAddsDifferentTasksAndCanFindThemById(){
		Task task = manager.createTask(new Task("Имя", "Описание"));
		Epic epic = manager.createEpic(new Epic ("Имя1","Описание1"));
		Subtask subtask = manager.createSubtask(new Subtask("Имя2", "Описание2", 2));
		Task getTask = manager.getTask(1);
		Epic getEpic = manager.getEpic(2);
		Subtask getSubtask = manager.getSubtask(3);
		assertNotNull(task);
		assertNotNull(epic);
		assertNotNull(subtask);
		assertNotNull(getTask);
		assertNotNull(getEpic);
		assertNotNull(getSubtask);
	}

	@Test
	public void setIdAndGeneratedIdTasksDoNotClash() {
		Map<Integer, Task> tasks = new HashMap<>();
		Task task = manager.createTask(new Task("Имя", "Описание"));
		Task task2 = new Task("Имя1", "Описание1", Status.DONE,1);
		tasks.put(task.getId(), task);
		tasks.put(task2.getId(), task2);
		assertEquals(1, tasks.size());

	}

	@Test
	public void taskDoesNotChangeAfterBeingAddedIntoManager() {
		Task task = new Task ("Имя", "Описание",Status.NEW, 1);
		Task task2 = manager.createTask(task);
		assertEquals(task.getName(), task2.getName());
		assertEquals(task.getDescription(), task2.getDescription());
		assertEquals(task.getStatus(), task2.getStatus());
		assertEquals(task.getId(), task2.getId());


	}

	@Test
	public void epicsShouldNotContainIrrelevantSubtaskIds() {
		Epic epic = manager.createEpic(new Epic ("Имя1","Описание1"));
		Subtask subtask = manager.createSubtask(new Subtask("Имя2", "Описание2", 1));
		Subtask subtask2 = manager.createSubtask(new Subtask("Имя3", "Описание3", 1));
		manager.clearSubtasks();
		assertEquals(0, epic.getSubtaskIds().size());

	}

	@Test
	public void epicStatusChangeWorksCorrectly() {
		Epic epic = manager.createEpic(new Epic ("name1", "desc1"));
		Subtask subtask1 = manager.createSubtask(new Subtask("subname1", "subdesc1", Status.NEW, 2, 1));
		Subtask subtask2 = manager.createSubtask(new Subtask("subname2", "subdesc2", Status.NEW, 3, 1));
		assertEquals(Status.NEW, epic.getStatus());
		manager.updateSubtask(new Subtask("subname1", "subdesc1", Status.DONE, 2, 1));
		manager.updateSubtask(new Subtask("subname2", "subdesc2", Status.DONE, 3, 1));
		assertEquals(Status.DONE, epic.getStatus());
		manager.updateSubtask(new Subtask("subname1", "subdesc1", Status.NEW, 2, 1));
		assertEquals(Status.IN_PROGRESS, epic.getStatus());
		manager.updateSubtask(new Subtask("subname1", "subdesc1", Status.IN_PROGRESS, 2, 1));
		manager.updateSubtask(new Subtask("subname2", "subdesc2", Status.IN_PROGRESS, 3, 1));
		assertEquals(Status.IN_PROGRESS, epic.getStatus());
	}

	@Test
	public void subtaskAlwaysHasEpic() {
		Epic epic = manager.createEpic(new Epic("name1", "desc1"));
		Subtask subtask = manager.createSubtask(new Subtask("subname", "subdesc", 1));
		assertEquals(epic.getId(), subtask.getEpicId());
	}

	@Test
	public void overlapTesterWorksCorrectly() {
		Task task = manager.createTask(new Task("name1", "desc1", 60, "01.01.2020.15:00"));
		Task task2 = manager.createTask(new Task("name2", "desc2", 60, "01.01.2020.15:30"));
		Task task3 = manager.createTask(new Task("name3", "desc3", 60, "02.02.2020.16:00"));
		Task task4 = manager.createTask(new Task("name4", "desc4", 90, "02.02.2020.15:00"));
		Task task5 = manager.createTask(new Task("name5", "desc5", 60, "03.03.2020.17:00"));
		Task task6 = manager.createTask(new Task("name6", "desc6", 60, "03.03.2020.17:00"));
		Task task7 = manager.createTask(new Task("name7", "desc7", 60, "04.04.2020.20:00"));
		Task task8 = manager.createTask(new Task("name8", "desc8", 30, "04.04.2020.20:00"));
		Task task9 = manager.createTask(new Task("name9", "desc9", 60, "05.05.2020.23:00"));
		Task task10 = manager.createTask(new Task("name10", "desc10", 30, "05.05.2020.23:30"));
		assertNotNull(task);
		assertNull(task2);
		assertNotNull(task3);
		assertNull(task4);
		assertNotNull(task5);
		assertNull(task6);
		assertNotNull(task7);
		assertNull(task8);
		assertNotNull(task9);
		assertNull(task10);

	}

}
