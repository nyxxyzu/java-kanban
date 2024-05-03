package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.NotFoundException;
import tasks.Epic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		ServerOperation operation = getOperation(exchange.getRequestMethod());

		switch (operation) {
			case GET: {
				handleGetEpics(exchange);
				break;
			}
			case POST: {
				handleCreateEpic(exchange);
				break;
			}
			case DELETE: {
				handleDeleteEpic(exchange);
				break;
			}
			default:
				sendText(exchange, "Такой операции не существует", 404);
		}
	}

	private void handleGetEpics(HttpExchange exchange) throws IOException {
		String[] path = exchange.getRequestURI().getPath().split("/");
		String response = "";

		if (path.length == 2) {
			response = manager.getAllEpics().stream()
					.map(task -> gson.toJson(task))
					.collect(Collectors.joining("\n"));
		}
		if (path.length == 3) {
			try {
				Epic epic = manager.getEpic(getId(exchange).get());
				response = gson.toJson(epic);
			} catch (NotFoundException e) {
				sendNotFound(exchange);
			}
		}
		if (path.length == 4) {
			if (path[3].equals("subtasks")) {
				try {
					Epic epic = manager.getEpic(getId(exchange).get());
					response = gson.toJson(manager.getAllSubtasksByEpic(epic.getId()));
				} catch (NotFoundException e) {
					sendNotFound(exchange);
				}
			} else {
				sendText(exchange, "Эндпоинт не найден",404);
			}
		}
		sendText(exchange, response, 200);
	}

	private void handleDeleteEpic(HttpExchange exchange) throws IOException {
		try {
			manager.removeEpicById(getId(exchange).get());
			sendText(exchange, "Эпик успешно удален.", 200);
		} catch (NotFoundException e) {
			sendNotFound(exchange);
		}
	}

	private void handleCreateEpic(HttpExchange exchange) throws IOException {
		String body = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
		Epic epic = gson.fromJson(body, Epic.class);
		epic.setSubtaskIds(new ArrayList<>());
		manager.createEpic(epic);
		sendText(exchange, "Эпик успешно создан.", 201);


	}
}
