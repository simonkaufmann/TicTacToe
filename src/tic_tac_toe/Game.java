package tic_tac_toe;
import java.util.ArrayList;
import java.util.Random;

import tic_tac_toe.Test_State;

public class Game {
	
	int player;
	Model model;
	ArrayList<State> states;
	
	public Game(int player, Model m) {
		this.player = player;
		this.model = m;
		this.states = new ArrayList<State>();
		this.states.add(new State(new Integer[] {0, 0, 0, 0, 0, 0, 0, 0, 0}));
	}
	
	// Returns true if move is possible and false if move is not possible
	public boolean otherPlayerField(int field) {		
		// Get board of last state
		Integer[] board = this.states.get(this.states.size() - 1).get();
		
		if (board[field] != 0) {
			return false;
		}
		
		if (this.player == 1) {
			board[field] = 2;
		} else {
			board[field] = 1;
		}
	
		this.states.add(new State(board));
		
		return true;
	}
	
	public void otherPlayerMove(State s) {
		if (s != null) {
			this.states.add(s);
		}
	}
	
	public ArrayList<State> getStates() {
		return this.states;
	}
	
	// Returns State of move made or null if no move possible or game already ended
	public State nextStep(State s) {
		otherPlayerMove(s);
		
		return nextStep();
	}
	
	// ' ': not finished, 'd': draw, 'w': win, 'l': lose
	public char result() {
		State last = this.states.get(this.states.size() - 1);
		int res = last.result();
		if (res == -1) {
			return ' ';
		} else if (res == 0) {
			return 'd';
		} else if (res != player) {
			return 'l';
		} else if (res == player) {
			return 'w';
		}
		return ' '; // not to happen
	}
	
	public boolean updateModel() {
		char res = this.result();
		if (res == ' ') {
			return false;
		} else if (res == 'w') {
			this.model.updateModelWin(states);
		} else if (res == 'l') {
			this.model.updateModelLose(states);
		}
		
		return true;
	}
	
	// Returns State of move made or null if no move possible or game already ended
	public State nextStep() {		
		State last = this.states.get(this.states.size() - 1);
		ArrayList<State> moves = last.nextSteps(this.player);
		
		// Abort if game is already decided
		if (last.result() != -1) {
			return null;
		}
		
		// no moves available
		if (moves.size() == 0) {
			return null;
		}
		
		// desirability
		ArrayList<Double> des = new ArrayList<Double>();
		double sum = 0.0;
		
		// Get desirability for every move
		for (State s: moves) {
			Double d = this.model.getDesirability(s);
			des.add(d);
			sum += d; 
		}
		
		// Normalise desirability and make accumulative to get list with values from 0 to 1
		double old = 0.0;
		for (int i = 0; i < des.size(); i++) {
			Double f = des.get(i);
			f = f / sum + old;
			old = f;
			des.set(i, f);
		}
		
		// Generate random Double from 0 to 1
		Random r = new Random();
		Double rand = r.nextDouble();
		
		// Check in which range of accumulative list the random number falls
		int i = 0;
		for (i = 0; i < des.size(); i++) {
			if (rand <= des.get(i)) {
				break;
			}
		}
		
		// Returns move with probability corresponding to desirability
		this.states.add(moves.get(i));
		
		return moves.get(i);
	}
	
	public void resetGame() {
		this.states = new ArrayList<State>();
		this.states.add(new State(new Integer[] {0,0,0,0,0,0,0,0,0}));
	}
	
	public Model getModel() {
		return this.model;
	}

}
