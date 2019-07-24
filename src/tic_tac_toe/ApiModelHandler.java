package tic_tac_toe;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import tic_tac_toe.Uri;

class ApiModelHandler implements HttpHandler {

    private final GameController gc;

    public ApiModelHandler(GameController gc) {
    	this.gc = gc;
    }
    
    @SuppressWarnings("unchecked")
	private void getPerformance(int player, HttpExchange exchange) throws IOException {
		ArrayList<PerformanceResult> performance = gc.getPerformance(player);
		ArrayList<JSONObject> jsonPerformance = new ArrayList<JSONObject>();
		
		for (PerformanceResult p: performance) {
			JSONObject jsonP = new JSONObject();
			jsonP.put("win", p.getWin() / (double) p.getTotal());
			jsonP.put("lose", p.getLose() / (double) p.getTotal());
			jsonP.put("draw", p.getDraw() / (double) p.getTotal());
			jsonP.put("trainingIterations", p.getTrainingIterations());
			jsonPerformance.add(jsonP);
		}
		
		// Return response state
		JSONObject json = new JSONObject();
		JSONArray jarray = new JSONArray();

		for (JSONObject j: jsonPerformance) {
			jarray.add(j);
		}
		
		json.put("performance", jarray);
		
		String response = json.toJSONString();
		exchange.getResponseHeaders().add("Content-type", "application/json");
		exchange.sendResponseHeaders(200, response.getBytes().length);
		OutputStream os = exchange.getResponseBody();
		os.write(response.getBytes());
		os.close();
    }
    
	private void getModelSettings(HttpExchange exchange) throws IOException {
		ModelSettings ms = gc.getModelSettings();
		
		JSONObject json = new JSONObject();
		json.put("alpha", ms.getAlpha());
		json.put("chance-random-move", ms.getChanceRandomMove());

		String response = json.toJSONString();
		exchange.sendResponseHeaders(200, response.getBytes().length);
		OutputStream os = exchange.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}
    
	private void setAlpha(HttpExchange exchange) throws IOException {
		InputStream in = exchange.getRequestBody();
		String body = Socket.inputStreamToString(in);
		try {
			JSONObject json = (JSONObject) new JSONParser().parse(body);
			long alpha = (long) json.get("alpha");
			gc.setAlpha(alpha);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		String response = "OK";
		exchange.sendResponseHeaders(200, response.getBytes().length);
		OutputStream os = exchange.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}
	
	private void setChanceRandomMove(HttpExchange exchange) throws IOException {
		InputStream in = exchange.getRequestBody();
		String body = Socket.inputStreamToString(in);
		try {
			JSONObject json = (JSONObject) new JSONParser().parse(body);
			long randomMove = (long) json.get("chance-random-move");
			gc.setChanceRandomMove(randomMove);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		String response = "OK";
		exchange.sendResponseHeaders(200, response.getBytes().length);
		OutputStream os = exchange.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}
	
	private void startTraining(HttpExchange exchange) throws IOException {
		gc.startTraining();
		
		String response = "OK";
		exchange.sendResponseHeaders(200, response.getBytes().length);
		OutputStream os = exchange.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}
	
	private void stopTraining(HttpExchange exchange) throws IOException {
		gc.stopTraining();
		
		String response = "OK";
		exchange.sendResponseHeaders(200, response.getBytes().length);
		OutputStream os = exchange.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}

	private void defaultHandler(HttpExchange exchange) throws IOException {
		String response = "Path not recognised";
		exchange.sendResponseHeaders(200, response.getBytes().length);
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
		// path[2] is always "model" because handler is set for it
		
		if (path.length >= 3) {
			switch (path[3]) {
				case "get-performanceX":
					getPerformance(State.PLAYER_X, exchange);
					break;
				case "get-performanceO":
					getPerformance(State.PLAYER_O, exchange);
					break;
				case "get-model-settings":
					getModelSettings(exchange);
					break;
				case "set-alpha":
					setAlpha(exchange);
					break;
				case "set-chance-random-move":
					setChanceRandomMove(exchange);
					break;
				case "start-training":
					startTraining(exchange);
					break;
				case "stop-training":
					stopTraining(exchange);
					break;
				default:
					defaultHandler(exchange);
			}
		}
    }
}