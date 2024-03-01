package Tasks;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

	@Test
	public void EpicsShouldBeEqualIfTheirIdIsEqual() {
		Task task1 = new Epic("Имя1","Описание1", 1);
		Task task2 = new Epic("Имя2", "Описание2", 1);
		assertEquals(task1, task2);
	}
}