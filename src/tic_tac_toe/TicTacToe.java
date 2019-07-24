package tic_tac_toe;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class TicTacToe {
	
	Game game;
	Model model;
	int player = State.PLAYER_X;
	
	String fn1 = "model1.dat";
	String fn2 = "model2.dat";
	boolean fn = false;
	
	public static void main(String[] args) {
		TicTacToe tic = new TicTacToe();
		tic.start();
	}
	
	public void start() {
		model = Model.importModel("model1.dat");
		
		Socket soc = new Socket();
		soc.startServer(model);
		
		game = new Game();
		
		for (int i = 0; i < 50000; i++) {
			System.out.println(model.testPerformance(1000, State.PLAYER_O).toString());
			
			// Export model
			if (i % 50 == 0) {
				if (fn) {
					model.exportModel(fn1);
					fn = !fn;
				} else {
					model.exportModel(fn2);
					fn = !fn;
				}
			}
				
			for (int j = 0; j < 10; j++) {
				model.trainModel(100);
			}
		}
		
	}
	
}
