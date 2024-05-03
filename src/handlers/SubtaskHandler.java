package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.NotFoundException;
import tasks.Subtask;

import java.io.IOException;
import java.util.stream.Collectors;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		ServerOperation operation = getOperation(exchange.getRequestMethod());

		switch (operation) {
			case GET: {
				handleGetSubtasks(exchange);
				break;
			}
			case POST: {
				handleCreateSubtask(exchange);
				break;
			}
			case DELETE: {
				handleDeleteSubtask(exchange);
				break;
			}
			default:
				sendText(exchange, "Такой операции не существует", 404);
		}
	}

	private void handleGetSubtasks(HttpExchange exchange) throws IOException {
		String[] path = exchange.getRequestURI().getPath().split("/");
		String response = "";
		if (path.length == 2) {
			response = manager.getAllSubtasks().stream()
					.map(task -> gson.toJson(task))
					.collect(Collectors.joining("\n"));
		}
		if (path.length == 3) {
			try {
				Subtask subtask = manager.getSubtask(getId(exchange).get());
				response = gson.toJson(subtask);
			} catch (NotFoundException e) {
				sendNotFound(exchange);
			}
		}
		sendText(exchange, response, 200);
	}

	private void handleDeleteSubtask(HttpExchange exchange) throws IOException {
		try {
			manager.removeSubtaskById(getId(exchange).get());
			sendText(exchange, "Сабтаск успешно удален", 200);
		} catch (NotFoundException e) {
			sendNotFound(exchange);
		}
	}

	private void handleCreateSubtask(HttpExchange exchange) throws IOException {
		String body = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
		Subtask subtask = gson.fromJson(body, Subtask.class);
		Subtask addedSubtask = null;
		if (subtask.getEpicId() != 0) {
			if (subtask.getId() != 0) {
				try {
					addedSubtask = manager.updateSubtask(subtask);
				} catch (NotFoundException e) {
					sendNotFound(exchange);
				}
			} else {
				addedSubtask = manager.createSubtask(subtask);
			}
			if (addedSubtask != null) {
				sendText(exchange, "Сабтаск успешно добавлен.", 201);
			} else {
				sendHasOverlaps(exchange);
			}
		} else {
			sendText(exchange,"Не введен номер эпика.",404);
		}
	}
}
