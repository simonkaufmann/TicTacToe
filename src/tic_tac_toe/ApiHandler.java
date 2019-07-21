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

    private final TicTacToe ticTac;

    public ApiHandler(TicTacToe tic) {
        ticTac = tic;
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
	
	@SuppressWarnings("unchecked")
	private void sendMove(HttpExchange exchange) throws IOException{
		InputStream in = exchange.getRequestBody();
		String body = Socket.inputStreamToString(in);
		State returnState = State.emptyState();
		try {
			JSONObject json = (JSONObject) new JSONParser().parse(body);
			long field = (long) json.get("field");
			returnState = ticTac.sendMove((int) field);
			
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
	private void getMove(HttpExchange exchange) throws IOException {
		State returnState = ticTac.getMove();
		
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
				default:
					defaultHandler(exchange);
			}
		}
    }
}