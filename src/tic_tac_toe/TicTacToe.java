package tic_tac_toe;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class TicTacToe {
	
	GameController gc;
	Model model;
	static String port;
	
	public static void main(String[] args) {
		if (args.length >= 1) {
			port = args[0];
		} else {
			port = "3001";
		}
		System.out.println("port: " + port);
		
		TicTacToe tic = new TicTacToe();
		tic.start();
	}
	
	public void start() {
		model = new Model();//Model.importModel("model1.dat");
		gc = new GameController(model);
		
		Socket soc = new Socket(Integer.parseInt(port));
		soc.startServer(gc);

		gc.training();
	}

}
