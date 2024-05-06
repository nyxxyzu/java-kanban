package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		ServerOperation operation = getOperation(exchange.getRequestMethod());

		if (operation == ServerOperation.GET) {
			handleHistory(exchange);
		} else {
			sendText(exchange, "Такой операции не существует", 404);
		}
	}

	private void handleHistory(HttpExchange exchange) throws IOException {
		String response = gson.toJson(manager.getHistory());
		sendText(exchange, response,200);


	}
}

