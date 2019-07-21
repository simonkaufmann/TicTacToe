package tic_tac_toe;

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
		Socket soc = new Socket();
		soc.startServer(this);
		
		model = Model.importModel("model1.dat");
		
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
	
	public void startGame() {
		game = new Game();
	}
	
	public State sendMove(int field) {
		State state = game.getMove();
		state.setField(field, player);
		game.addMove(state);
		
		State nextMove = model.getNextMove(state, State.switchPlayer(player), false);
		game.addMove(nextMove);
		
		return nextMove;
	}
	
	public State getMove() {
		return game.getMove();
	}
	
	public ArrayList<PerformanceResult> getPerformance() {
		return model.getPerformance();
	}
	
}
