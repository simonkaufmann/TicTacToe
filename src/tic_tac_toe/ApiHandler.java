package tic_tac_toe;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import tic_tac_toe.Uri;

class ApiHandler implements HttpHandler {

    private final GameController gc;

    public ApiHandler(GameController gc) {
        this.gc = gc;
    }
    
	@SuppressWarnings("unchecked")
	private void startGame(HttpExchange exchange) throws IOException {
		String id = gc.startGame();
		
		JSONObject json = new JSONObject();
		json.put("id", id);
		
		String response = json.toJSONString();
		exchange.getResponseHeaders().add("Content-type", "application/json");
		exchange.sendResponseHeaders(200, response.getBytes().length);
		OutputStream os = exchange.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}
	
	@SuppressWarnings("unchecked")
	private void sendMove(String id, HttpExchange exchange) throws IOException{
		InputStream in = exchange.getRequestBody();
		String body = Socket.inputStreamToString(in);
		State returnState = State.emptyState();
		try {
			JSONObject json = (JSONObject) new JSONParser().parse(body);
			long field = (long) json.get("field");
			returnState = gc.sendMove(id, (int) field);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		// Return response state
		JSONObject json = new JSONObject();
		JSONArray jarray = new JSONArray();

		Integer[] intState = returnState.get();
		for (int i = 0; i < intState.length; i++) {
			jarray.add(intState[i]);
		}
		
		json.put("state", jarray);
		json.put("result", Integer.toString(returnState.result()));
		
		String response = json.toJSONString();
		exchange.getResponseHeaders().add("Content-type", "application/json");
		exchange.sendResponseHeaders(200, response.getBytes().length);
		OutputStream os = exchange.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}
	
	@SuppressWarnings("unchecked")
	private void getMove(String id, HttpExchange exchange) throws IOException {
		State returnState = gc.getMove(id);
		
		// Return response state
		JSONObject json = new JSONObject();
		JSONArray jarray = new JSONArray();

		Integer[] intState = returnState.get();
		for (int i = 0; i < intState.length; i++) {
			jarray.add(intState[i]);
		}
		
		json.put("state", jarray);
		json.put("result", Integer.toString(returnState.result()));
		
		String response = json.toJSONString();
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
		Uri uri = Uri.parseUri(stringUri);

		String[] path = uri.path;

		// path[1] is always "api" because handler is set for it
		// path[2] is always "game" because handler is set for it
		
		String id = "";
		if (path.length >= 4) {
			if (path[3].equals("start-game")) {
				startGame(exchange);
			} else {
				id = path[3];
			}
		}
		
		if (path.length >= 5) {
			switch (path[4]) {
				case "send-move":
					System.out.println("send-move");
					sendMove(id, exchange);
					break;
				case "get-move":
					getMove(id, exchange);
				default:
					defaultHandler(exchange);
			}
		}
    }
}