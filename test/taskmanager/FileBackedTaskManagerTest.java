package taskmanager;

import org.junit.jupiter.api.BeforeEach;
import tasks.Task;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

	final File file = File.createTempFile("test","csv");
	final File testFile = new File("test.txt");

	public FileBackedTaskManagerTest() throws IOException {

	}

	@BeforeEach
	void init() {
		super.init(new FileBackedTaskManager(file));
	}

	@Test
	void managerSavesTasksIntoFile() {
		Task task1 = manager.createTask(new Task("name1","desc1"));
		Task task2 = manager.createTask(new Task("name2","desc2"));
		Task task3 = manager.createTask(new Task("name3","desc3"));
		List<String> strings = new LinkedList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			while(reader.ready()) {
				strings.add(reader.readLine());
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		assertEquals(task1.toString(),strings.get(1));
		assertEquals(task2.toString(),strings.get(2));
		assertEquals(task3.toString(),strings.get(3));

	}

	@Test
	void managerLoadsTasksFromFile() {
		Task task1 = manager.createTask(new Task("name1","desc1"));
		Task task2 = manager.createTask(new Task("name2","desc2"));
		Task task3 = manager.createTask(new Task("name3","desc3"));
		FileBackedTaskManager newManager = FileBackedTaskManager.loadFromFile(file);
		assertEquals(task1.toString(),newManager.getTask(1).toString());
		assertEquals(task2.toString(),newManager.getTask(2).toString());
		assertEquals(task3.toString(),newManager.getTask(3).toString());

	}

	@Test
	void testExceptions() {
		assertThrows(ManagerLoadException.class, () -> {
			manager.loadFromFile(testFile);
		}, "Ошибка при загрузке из несуществующего файла");
	}
}
