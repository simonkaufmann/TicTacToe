package tic_tac_toe;

public class TicTacToe {
	
	Game game;
	Model model;
	int player = State.PLAYER_X;
	
	public static void main(String[] args) {
		TicTacToe tic = new TicTacToe();
		tic.start();
	}
	
	public void start() {
		Socket soc = new Socket();
		soc.startServer(this);
		
		model = new Model();
		
		game = new Game();
		
		for (int i = 0; i < 50000; i++) {
			System.out.println(model.testPerformance(1000, State.PLAYER_O).toString());
			for (int j = 0; j < 10; j++) {
				model.trainModel(10);
				//System.out.println(j * 1000);
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
	
}
