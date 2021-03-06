package tic_tac_toe;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class GameController {

	HashMap<String, Game> games;
	Model model;
	volatile boolean trainingActive = true;
	
	String fn1 = "model1.dat";
	String fn2 = "model2.dat";
	boolean fn = false;

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
	
	public String startGame(int humanPlayer) {
		String id = randomAlphaNumeric(15);
		
		Game game = new Game(humanPlayer);
		games.put(id, game);
		
		if (humanPlayer == State.PLAYER_O)
		{
			State s = game.getMove();
			State nextMove = model.getNextMove(s, State.switchPlayer(humanPlayer), false, true);
			game.addMove(nextMove);
		}
		return id;
	}
	
	public State sendMove(String id, int field) {
		Game game = games.get(id);
		if (game == null) {
			return null;
		}
		
		State state = game.getMove();
		int player = game.getHumanPlayer();
		state.setField(field, player);
		game.addMove(state);
		
		State nextMove = model.getNextMove(state, State.switchPlayer(player), false, true);
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
	
	public ArrayList<PerformanceResult> getPerformance(int player) {
		return model.getPerformance(player);
	}
	
	public void setAlpha(double alpha) {
		model.setAlpha(alpha);
	}
	
	public void setChanceRandomMove(double randomMove) {
		model.setChanceRandomMove(randomMove);
	}
	
	public void startTraining() {
		this.trainingActive = true;
	}
	
	public void stopTraining() {
		this.trainingActive = false;
	}
	
	public boolean getTraining () {
		return this.trainingActive;
	}
	
	public byte[] exportModelToByte() {
		return model.exportModelToByte();
	}
	
	public void training() {
		Game game = new Game();
		
		int i = 0;
		
		while (true) {
			if (trainingActive) {
				model.testPerformance(1000, State.PLAYER_O);
				model.testPerformance(1000, State.PLAYER_X);
				
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
			i++;
		}
	}
	
	public ModelSettings getModelSettings() {
		ModelSettings ms = new ModelSettings();
		ms.setAlpha(model.getAlpha());
		ms.setChanceRandomMove(model.getChanceRandomMove());
		return ms;
	}
	
}
