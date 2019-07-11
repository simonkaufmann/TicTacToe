package tic_tac_toe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
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
	
	static class ApiHandler implements HttpHandler {

	    private final TicTacToe ticTac;

	    public ApiHandler(TicTacToe m) {
	        ticTac = m;
	    }
	    
		private void startGame(HttpExchange exchange) throws IOException {
			ticTac.startGame();
			
			String response = "OK";
			exchange.getResponseHeaders().add("Content-type", "text/plain");
			exchange.sendResponseHeaders(200, response.getBytes().length);
			OutputStream os = exchange.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}
		
		private void sendMove(HttpExchange exchange) throws IOException{
			InputStream in = exchange.getRequestBody();
			String body = inputStreamToString(in);
			
			try {
				JSONObject json = (JSONObject) new JSONParser().parse(body);
				long field = (long) json.get("field");
				ticTac.sendMove((int) field);
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			String response = "OK";
			exchange.getResponseHeaders().add("Content-type", "text/plain");
			exchange.sendResponseHeaders(200, response.getBytes().length);
			OutputStream os = exchange.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}
		
		private void getMove(HttpExchange exchange) throws IOException {
			State state = ticTac.getMove();
			
			JSONObject json = new JSONObject();
			JSONArray jarray = new JSONArray();

			Integer[] intState = state.get();
			for (int i = 0; i < intState.length; i++) {
				jarray.add(intState[i]);
			}
			
			json.put("state", jarray);
			
			System.out.println(json.toJSONString());
			
			String response = "OK";
			exchange.getResponseHeaders().add("Content-type", "application/json");
			exchange.sendResponseHeaders(200, response.getBytes().length);
			OutputStream os = exchange.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}
		
		private void defaultHandler(HttpExchange exchange) throws IOException {
			String response = "Path not recognised";
			exchange.sendResponseHeaders(404, response.getBytes().length);
			OutputStream os = exchange.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}

		@Override
	    public void handle(HttpExchange exchange) throws IOException {    	
    		String stringUri = exchange.getRequestURI().toString();
    		Uri uri = parseUri(stringUri);

    		String[] path = uri.path;

    		// path[0] is always "api" because handler is set for it
    		
    		if (path.length >= 3) {
    			switch (path[2]) {
    				case "start-game":
    					startGame(exchange);
    					break;
    				case "send-move":
    					sendMove(exchange);
    					break;
    				case "get-move":
    					getMove(exchange);
    					break;
    				default:
    					defaultHandler(exchange);
    			}
    		}
	    }
	}
	
	private static String inputStreamToString(InputStream t) {
		try {
			InputStreamReader isr;
			isr = new InputStreamReader(t,"utf-8");
			BufferedReader br = new BufferedReader(isr);
	
			int b;
			StringBuilder buf = new StringBuilder(512);
			while ((b = br.read()) != -1) {
			    buf.append((char) b);
			}
	
			br.close();
			isr.close();
			return buf.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	private static Uri parseUri(String uri) {
		String[] split = uri.split("#");
		
		uri = split[0];
		String fragment = "";
		for (int i = 1; i < split.length; i++) {
			fragment = fragment + split[i];
		}
		
		split = uri.split("\\?");
		String path = split[0];
		String query = "";
		for (int i = 1; i < split.length; i++) {
			query = query + split[i];
		}
		
		String[] splitPath = path.split("/");
		
		Uri parsed = new Uri(splitPath, query, fragment);
		return parsed;
	}

	public void startServer(TicTacToe ticTac) {
		try {
			HttpServer server = HttpServer.create(new InetSocketAddress(3001), 0);
			HttpContext api = server.createContext("/api");
			api.setHandler(new ApiHandler(ticTac));
			server.start();
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
	}
}
