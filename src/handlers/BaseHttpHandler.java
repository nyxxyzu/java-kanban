package handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import taskmanager.Managers;
import taskmanager.TaskManager;
import typeadapters.DurationAdapter;
import typeadapters.ZonedDateTimeAdapter;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Optional;

public class BaseHttpHandler {

	protected static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
	protected TaskManager manager = Managers.getDefault();
	Gson gson = new GsonBuilder()
			.registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
			.registerTypeAdapter(Duration.class, new DurationAdapter())
			.setPrettyPrinting()
			.create();

	protected void sendText(HttpExchange exchange, String responseString, int responseCode) throws IOException {
		try (OutputStream os = exchange.getResponseBody()) {
			exchange.sendResponseHeaders(responseCode, 0);
			os.write(responseString.getBytes(DEFAULT_CHARSET));
		}
		exchange.close();
	}

	protected void sendNotFound(HttpExchange exchange) throws IOException {
		try (OutputStream os = exchange.getResponseBody()) {
			exchange.sendResponseHeaders(404,0);
			os.write("Задача не найдена.".getBytes(DEFAULT_CHARSET));
		}
		exchange.close();
	}

	protected void sendHasOverlaps(HttpExchange exchange) throws IOException {
		try (OutputStream os = exchange.getResponseBody()) {
			exchange.sendResponseHeaders(406,0);
			os.write("Задача пересекается с другой по времени.".getBytes(DEFAULT_CHARSET));
		}
		exchange.close();
	}

	protected ServerOperation getOperation(String requestMethod) {
		if (requestMethod.equals("GET")) {
			return ServerOperation.GET;
		}
		if (requestMethod.equals("POST")) {
			return ServerOperation.POST;
		}
		if (requestMethod.equals("DELETE")) {
			return ServerOperation.DELETE;
		}
		return ServerOperation.UNKNOWN;
	}

	protected Optional<Integer> getId(HttpExchange exchange) {
		String[] pathParts = exchange.getRequestURI().getPath().split("/");
		try {
			return Optional.of(Integer.parseInt(pathParts[2]));
		} catch (NumberFormatException exception) {
			return Optional.empty();
		}
	}
}
