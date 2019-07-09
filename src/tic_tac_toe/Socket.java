package tic_tac_toe;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

public class Socket {

	public static void startServer() {
		try {
			HttpServer server = HttpServer.create(new InetSocketAddress(3001), 0);
			HttpContext context = server.createContext("/");
			context.setHandler(Socket::handleRequest);
			server.start();
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
	}
	
	private static void handleRequest(HttpExchange exchange) throws IOException {
		String response = "My response";
		exchange.getResponseHeaders().add("Content-type", "application/json");
		exchange.sendResponseHeaders(200, response.getBytes().length);
		OutputStream os = exchange.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}
}
