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
	
	public static String inputStreamToString(InputStream t) {
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

	public void startServer(TicTacToe ticTac) {
		try {
			HttpServer server = HttpServer.create(new InetSocketAddress(3001), 0);
			HttpContext api = server.createContext("/api");
			HttpContext apiModel = server.createContext("/api/model");
			api.setHandler(new ApiHandler(ticTac));
			apiModel.setHandler(new ApiModelHandler(ticTac));
			server.start();
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
	}
}
