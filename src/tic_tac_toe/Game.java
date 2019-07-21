package tic_tac_toe;
import java.util.ArrayList;

public class Game {
	
	int player = 1;
	ArrayList<State> states;
	
	public Game() {
		this.states = new ArrayList<State>();
		this.states.add(State.emptyState());
	}
	
	public boolean addMove(State s) {
		if (this.result() == State.UNDECIDED) {
			states.add(s);
			player = State.switchPlayer(player);
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
