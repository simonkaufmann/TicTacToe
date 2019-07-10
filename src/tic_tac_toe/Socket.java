package tic_tac_toe;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

public class Socket {
	
	static class Uri {
		public String[] path;
		public String query;
		public String fragment;
		
		Uri(String[] p, String q, String f) {
			path = p;
			query = q;
			fragment = f;
		}
	}
	
	private static void startGame(HttpExchange exchange) throws IOException {
		String response = "OK";
		exchange.getResponseHeaders().add("Content-type", "application/json");
		exchange.sendResponseHeaders(200, response.getBytes().length);
		OutputStream os = exchange.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}
	
	private static void sendMove(HttpExchange exchange) throws IOException{
		String response = "OK";
		exchange.getResponseHeaders().add("Content-type", "application/json");
		exchange.sendResponseHeaders(200, response.getBytes().length);
		OutputStream os = exchange.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}
	
	private static void getMove(HttpExchange exchange) throws IOException {
		String response = "OK";
		exchange.getResponseHeaders().add("Content-type", "application/json");
		exchange.sendResponseHeaders(200, response.getBytes().length);
		OutputStream os = exchange.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}
	
	private static Uri parseUri(String uri) {
		String[] split = uri.split("#");
		
		uri = split[0];
		String fragment = "";
		for (int i = 1; i < split.length; i++) {
			fragment = fragment + split[i];
		}
		
		split = uri.split("?");
		String path = split[0];
		String query = "";
		for (int i = 1; i < split.length; i++) {
			query = query + split[i];
		}
		
		String[] splitPath = path.split("/");
		
		Uri parsed = new Uri(splitPath, query, fragment);
		return parsed;
	}
	
	private static void handleRequest(HttpExchange exchange) throws IOException {
		String stringUri = exchange.getRequestURI().toString();
		Uri uri = parseUri(stringUri);

		String[] path = uri.path;
		
		// path[0] is always "api" because handler is set for it
		
		if (path.length >= 2) {
			switch (path[1]) {
				case "start-game":
					startGame(exchange);
					break;
				case "send-move":
					sendMove(exchange);
					break;
				case "get-move":
					getMove(exchange);
					break;
			}
		}
	}

	public void startServer() {
		try {
			HttpServer server = HttpServer.create(new InetSocketAddress(3001), 0);
			HttpContext api = server.createContext("/api");
			api.setHandler(Socket::handleRequest);
			server.start();
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
	}
}
