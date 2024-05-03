package tasks;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

	@Test
	public void SubtasksShouldBeEqualIfTheirIdIsEqual() {
		Task epic = new Epic("Имя","Описание",1);
		Task task1 = new Subtask("Имя1","Описание1", Status.NEW, 1, 1);
		Task task2 = new Subtask("Имя2", "Описание2", Status.DONE, 1, 1);
		assertEquals(task1, task2);
	}
}