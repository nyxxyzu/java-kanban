package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.NotFoundException;
import tasks.Task;

import java.io.IOException;
import java.util.stream.Collectors;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		ServerOperation operation = getOperation(exchange.getRequestMethod());

		switch (operation) {
			case GET: {
				handleGetTasks(exchange);
				break;
			}
			case POST: {
				handleCreateTask(exchange);
				break;
			}
			case DELETE: {
				handleDeleteTask(exchange);
				break;
			}
			default:
				sendText(exchange, "Такой операции не существует", 404);
		}
	}

	private void handleGetTasks(HttpExchange exchange) throws IOException {
		String[] path = exchange.getRequestURI().getPath().split("/");
		String response = "";
		if (path.length == 2) {
			response = manager.getAllTasks().stream()
					.map(task -> gson.toJson(task))
					.collect(Collectors.joining("\n"));
		}
		if (path.length == 3) {
			try {
				Task task = manager.getTask(getId(exchange).get());
				response = gson.toJson(task);
			} catch (NotFoundException e) {
				sendNotFound(exchange);
			}
		}
		sendText(exchange, response, 200);
	}

	private void handleDeleteTask(HttpExchange exchange) throws IOException {
		try {
			manager.removeTaskById(getId(exchange).get());
			sendText(exchange, "Задача успешно удалена", 200);
		} catch (NotFoundException e) {
			sendNotFound(exchange);
		}
	}

	private void handleCreateTask(HttpExchange exchange) throws IOException {
		String body = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
		Task task = gson.fromJson(body, Task.class);
		Task addedTask = null;
		if (task.getId() != 0) {
			try {
				addedTask = manager.updateTask(task);
			} catch (NotFoundException e) {
				sendNotFound(exchange);
			}
		} else {
			addedTask = manager.createTask(task);
		}
		if (addedTask != null) {
			sendText(exchange,"Задача успешно добавлена",201);
		} else {
			sendHasOverlaps(exchange);
		}
	}
}
