package taskmanager;

import com.sun.net.httpserver.HttpServer;
import handlers.*;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

	private static final int PORT = 8080;

	public static void main(String[] args) throws IOException {
		HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
		server.createContext("/tasks", new TaskHandler());
		server.createContext("/epics", new EpicHandler());
		server.createContext("/subtasks", new SubtaskHandler());
		server.createContext("/prioritized", new PrioritizedHandler());
		server.createContext("/history", new HistoryHandler());
		server.start();
		System.out.println("HTTP-сервер запущен на " + PORT + " порту!");

	}
}

