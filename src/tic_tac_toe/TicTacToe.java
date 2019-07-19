package tic_tac_toe;

import tic_tac_toe.Player;

public class TicTacToe {
	
	private Player game;
	
	public static void main(String[] args) {
		TicTacToe ticTac = new TicTacToe();
		ticTac.startGame();
		
		Socket soc = new Socket();
		soc.startServer(ticTac);
	}
	
	public void startGame() {
		Model m = new Model();
		game = new Player(2, m);
	}
	
	public boolean sendMove(int s) {
		return game.otherPlayerField(s);
	}
	
	public State getMove() {
		return game.nextStep(false);
	}
	
	public char getResult() {
		return game.result();
	}

}
