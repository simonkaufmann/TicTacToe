package tic_tac_toe;

import tic_tac_toe.Game;

public class TicTacToe {
	
	public static void main(String[] args) {
		Model m = new Model();
		
		for (int i = 0; i < 50; i++) {
			for (int j = 0; j < 1000; j++) {
				m.trainModel(1);
				System.out.println(i * 1000);
			}
			System.out.println(m.testPerformance(500, State.PLAYER_X).toString());
		}
		
		//TicTacToe ticTac = new TicTacToe();
		//ticTac.startGame();
		
		//Socket soc = new Socket();
		//soc.startServer(ticTac);
	}
	
	/*public void startGame() {
		Model m = new Model();
		game = new Game(2, m);
	}
	
	public boolean sendMove(int s) {
		return game.otherPlayerField(s);
	}
	
	public State getMove() {
		return game.nextStep(false);
	}
	
	public char getResult() {
		return game.result();
	}*/

}
