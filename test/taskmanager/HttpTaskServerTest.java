package taskmanager;
import com.google.gson.*;
import com.sun.net.httpserver.HttpServer;
import handlers.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import typeadapters.DurationAdapter;
import typeadapters.ZonedDateTimeAdapter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerTest {


	HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
	TaskManager manager = Managers.getDefault();

	Gson gson = new GsonBuilder()
			.registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
			.registerTypeAdapter(Duration.class, new DurationAdapter())
			.setPrettyPrinting()
			.create();

	public HttpTaskServerTest() throws IOException {
	}


	@BeforeEach
	public void setUp() {
		manager.setTaskId(1);
		manager.clearTasks();
		manager.clearSubtasks();
		manager.clearEpics();
		server.createContext("/tasks", new TaskHandler());
		server.createContext("/epics", new EpicHandler());
		server.createContext("/subtasks", new SubtaskHandler());
		server.createContext("/prioritized", new PrioritizedHandler());
		server.createContext("/history", new HistoryHandler());
		server.start();
	}

	@AfterEach
	public void shutDown() {
		server.stop(0);
	}

	@Test
	void testCreateTask() throws IOException, InterruptedException {
		Task task = new Task("task","desc");
		String taskJson = gson.toJson(task);
		HttpClient client = HttpClient.newHttpClient();
		URI url = URI.create("http://localhost:8080/tasks");
		HttpRequest request = HttpRequest.newBuilder()
				.uri(url)
				.POST(HttpRequest.BodyPublishers.ofString(taskJson))
				.build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		assertEquals(201, response.statusCode());
		Collection<Task> tasksFromManager = manager.getAllTasks();
		assertNotNull(tasksFromManager, "Задачи не возвращаются");
		assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
		assertEquals("task", manager.getTask(1).getName(), "Некорректное имя задачи");
	}

	@Test
	void testGetTask() throws IOException, InterruptedException {
		Task task = manager.createTask(new Task("test","desc"));
		String taskJson = gson.toJson(task);
		HttpClient client = HttpClient.newHttpClient();
		URI url = URI.create("http://localhost:8080/tasks");
		HttpRequest request = HttpRequest.newBuilder()
				.uri(url)
				.GET()
				.build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		assertEquals(200, response.statusCode());
		assertEquals(response.body(),taskJson);

	}

	@Test
	void testDeleteTask() throws IOException, InterruptedException {
		Task task = manager.createTask(new Task("test","desc"));
		HttpClient client = HttpClient.newHttpClient();
		URI url = URI.create("http://localhost:8080/tasks/1");
		HttpRequest request = HttpRequest.newBuilder()
				.uri(url)
				.DELETE()
				.build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		assertEquals(200, response.statusCode());
		Collection<Task> tasksFromManager = manager.getAllTasks();
		assertEquals(0, tasksFromManager.size());
	}

	@Test
	void testCreateSubtask() throws IOException, InterruptedException {
		Epic epic = manager.createEpic(new Epic("epic","desc"));
		Subtask subtask = new Subtask("subtask","desc",1);
		String subtaskJson = gson.toJson(subtask);
		HttpClient client = HttpClient.newHttpClient();
		URI url = URI.create("http://localhost:8080/subtasks");
		HttpRequest request = HttpRequest.newBuilder()
				.uri(url)
				.POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
				.build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		assertEquals(201, response.statusCode());
		Collection<Subtask> subtasksFromManager = manager.getAllSubtasks();
		assertNotNull(subtasksFromManager, "Задачи не возвращаются");
		assertEquals(1, subtasksFromManager.size(), "Некорректное количество задач");
		assertEquals("subtask", manager.getSubtask(2).getName(), "Некорректное имя задачи");
	}

	@Test
	void testGetSubtask() throws IOException, InterruptedException {
		Epic epic = manager.createEpic(new Epic("epic","desc"));
		Subtask subtask = manager.createSubtask(new Subtask("subtask","desc",1));
		String subtaskJson = gson.toJson(subtask);
		HttpClient client = HttpClient.newHttpClient();
		URI url = URI.create("http://localhost:8080/subtasks");
		HttpRequest request = HttpRequest.newBuilder()
				.uri(url)
				.GET()
				.build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		assertEquals(200, response.statusCode());
		assertEquals(response.body(),subtaskJson);

	}

	@Test
	void testDeleteSubtask() throws IOException, InterruptedException {
		Epic epic = manager.createEpic(new Epic("epic","desc"));
		Subtask subtask = manager.createSubtask(new Subtask("subtask","desc",1));
		HttpClient client = HttpClient.newHttpClient();
		URI url = URI.create("http://localhost:8080/subtasks/2");
		HttpRequest request = HttpRequest.newBuilder()
				.uri(url)
				.DELETE()
				.build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		assertEquals(200, response.statusCode());
		Collection<Subtask> subtasksFromManager = manager.getAllSubtasks();
		assertEquals(0, subtasksFromManager.size());
	}

	@Test
	void testCreateEpic() throws IOException, InterruptedException {
		Epic epic = new Epic("epic","desc");
		String epicJson = gson.toJson(epic);
		HttpClient client = HttpClient.newHttpClient();
		URI url = URI.create("http://localhost:8080/epics");
		HttpRequest request = HttpRequest.newBuilder()
				.uri(url)
				.POST(HttpRequest.BodyPublishers.ofString(epicJson))
				.build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		assertEquals(201, response.statusCode());
		Collection<Epic> epicsFromManager = manager.getAllEpics();
		assertNotNull(epicsFromManager, "Задачи не возвращаются");
		assertEquals(1, epicsFromManager.size(), "Некорректное количество задач");
		assertEquals("epic", manager.getEpic(1).getName(), "Некорректное имя задачи");
	}

	@Test
	void testGetEpic() throws IOException, InterruptedException {
		Epic epic = manager.createEpic(new Epic("epic","desc"));
		String epicJson = gson.toJson(epic);
		HttpClient client = HttpClient.newHttpClient();
		URI url = URI.create("http://localhost:8080/epics");
		HttpRequest request = HttpRequest.newBuilder()
				.uri(url)
				.GET()
				.build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		assertEquals(200, response.statusCode());
		assertEquals(response.body(),epicJson);

	}

	@Test
	void testDeleteEpic() throws IOException, InterruptedException {
		Epic epic = manager.createEpic(new Epic("epic","desc"));
		HttpClient client = HttpClient.newHttpClient();
		URI url = URI.create("http://localhost:8080/epics/1");
		HttpRequest request = HttpRequest.newBuilder()
				.uri(url)
				.DELETE()
				.build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		assertEquals(200, response.statusCode());
		Collection<Epic> epicsFromManager = manager.getAllEpics();
		assertEquals(0, epicsFromManager.size());
	}

	@Test
	void testGetHistory() throws IOException, InterruptedException {
		Task task1 = manager.createTask(new Task("task1","desc1"));
		Task task2 = manager.createTask(new Task("task2","desc2"));
		manager.getTask(2);
		manager.getTask(1);
		HttpClient client = HttpClient.newHttpClient();
		URI url = URI.create("http://localhost:8080/history");
		HttpRequest request = HttpRequest.newBuilder()
				.uri(url)
				.GET()
				.build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		assertEquals(200, response.statusCode());
		assertEquals(gson.toJson(manager.getHistory()), response.body());
	}

	@Test
	void testGetPrioritized() throws IOException, InterruptedException {
		Task task1 = manager.createTask(new Task("task1","desc1",30,"25.05.2005.16:00"));
		Task task2 = manager.createTask(new Task("task2","desc2",30,"25.05.2006.17:00"));
		HttpClient client = HttpClient.newHttpClient();
		URI url = URI.create("http://localhost:8080/prioritized");
		HttpRequest request = HttpRequest.newBuilder()
				.uri(url)
				.GET()
				.build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		assertEquals(200, response.statusCode());
		assertEquals(gson.toJson(manager.getPrioritizedTasks()), response.body());
	}
}

