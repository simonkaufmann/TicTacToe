package tic_tac_toe;
import java.util.ArrayList;
import java.util.Random;

public class Game {
	
	boolean training;
	int player = 1;
	Model model;
	ArrayList<State> states;
	
	public Game(Model m, boolean training) {
		this.model = m;
		this.states = new ArrayList<State>();
		this.states.add(State.emptyState());
		this.training = training;
	}
	
	public boolean addMove(State s) {
		if (this.result() == State.DRAW) {
			states.add(s);
			if (player == State.PLAYER_X) {
				player = State.PLAYER_O;
			} else {
				player = State.PLAYER_X;
			}
			return true;
		} else {
			return false;
		}
	}
	
	public State getMove() {
		return states.get(states.size() - 1);
	}
	
	public int result() {
		return this.getMove().result();
	}

}
