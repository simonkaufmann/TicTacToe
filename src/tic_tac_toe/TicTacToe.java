package tic_tac_toe;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class TicTacToe {
	
	GameController gc;
	Model model;
	
	public static void main(String[] args) {
		TicTacToe tic = new TicTacToe();
		tic.start();
	}
	
	public void start() {
		model = new Model();//Model.importModel("model1.dat");
		gc = new GameController(model);
		
		Socket soc = new Socket();
		soc.startServer(gc);

		gc.training();
	}

}
