package tic_tac_toe;

import tic_tac_toe.Game;

public class TicTacToe {
	
	private Game game;
	
	public static void main(String[] args) {
		TicTacToe ticTac = new TicTacToe();
		ticTac.startGame();
		
		Socket soc = new Socket();
		soc.startServer(ticTac);
	}
	
	public void startGame() {
		Model m = new Model();
		game = new Game(2, m);
	}
	
	public void sendMove(int s) {
		game.otherPlayerField(s);
	}
	
	public State getMove() {
		return(game.nextStep(false));
	}

}
