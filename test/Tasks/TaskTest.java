package Tasks;

import TaskManager.Status;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

	@Test
	public void TasksShouldBeEqualIfTheirIdIsEqual() {
		Task task1 = new Task("Имя","Описание", Status.NEW,1);
		Task task2 = new Task("Имя2","Описание2",Status.DONE,1);
		assertEquals(task1, task2);
	}


}