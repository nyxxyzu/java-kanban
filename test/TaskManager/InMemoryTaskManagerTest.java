package TaskManager;

import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

	InMemoryTaskManager manager = new InMemoryTaskManager();
	@Test
	public void epicCantBeAddedAsSubtask() {
		//HashMap<Integer, Subtask> subtasks = new HashMap<>();
		//Epic epic = new Epic("Имя", "Описание");
		//manager.createSubtask(epic);
		//Логика создания сабтаска не подразумевает возможности, что там окажется эпик. Несоответсвие типов.

	}

	@Test
	public void subtaskCantBeItsOwnEpic() {
		//Epic epic = new Epic("Имя", "Описание");
		//Subtask subtask = new Subtask("Имя1", "Описание1",1);
		//manager.createEpic(subtask);
		//Логика создания эпика не подразумевает возможности, что там окажется сабтаск. Несоответсвие типов.


	}
	@Test
	public void inMemoryTaskManagerAddsDifferentTasksAndCanFindThemById(){
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
		Task task = manager.createTask(new Task("Имя", "Описание"));
		Task task2 = new Task("Имя1", "Описание1", Status.DONE,1);
		assertEquals(1, manager.getTaskList().size());

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

}