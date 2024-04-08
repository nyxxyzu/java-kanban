package taskmanager;

import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

}
