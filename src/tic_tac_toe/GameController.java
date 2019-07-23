package tic_tac_toe;

import java.util.ArrayList;
import java.util.HashMap;

public class GameController {

	HashMap<String, Game> games;
	Model model;
	int player = State.PLAYER_X;

	private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	
	private static String randomAlphaNumeric(int count) {
		StringBuilder builder = new StringBuilder();
		while (count-- != 0) {
			int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
			builder.append(ALPHA_NUMERIC_STRING.charAt(character));
		}
		return builder.toString();
	}
	
	public GameController(Model m) {
		games = new HashMap<String, Game>();
		model = m;
	}
	
	public String startGame() {
		String id = randomAlphaNumeric(15);
		
		games.put(id, new Game());
		return id;
	}
	
	public State sendMove(String id, int field) {
		Game game = games.get(id);
		if (game == null) {
			return null;
		}
		
		State state = game.getMove();
		state.setField(field, player);
		game.addMove(state);
		
		State nextMove = model.getNextMove(state, State.switchPlayer(player), false);
		game.addMove(nextMove);
		
		return nextMove;
	}
	
	public State getMove(String id) {
		Game game = games.get(id);
		if (game == null) {
			return null;
		}
		return game.getMove();
	}
	
	public ArrayList<PerformanceResult> getPerformance() {
		return model.getPerformance();
	}
	
}