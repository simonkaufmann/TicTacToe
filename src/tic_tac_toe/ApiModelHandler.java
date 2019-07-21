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

    private final TicTacToe ticTac;

    public ApiModelHandler(TicTacToe tic) {
        ticTac = tic;
    }
    
    private void getPerformance(HttpExchange exchange) throws IOException {
		ArrayList<PerformanceResult> performance = ticTac.getPerformance();
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
		// path[2] is always "model" because handler is set for it
		
		if (path.length >= 3) {
			switch (path[3]) {
				case "get-performance":
					getPerformance(exchange);
					break;
				default:
					defaultHandler(exchange);
			}
		}
    }
}