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
	public State nextStep(boolean train, State s) {
		otherPlayerMove(s);
		
		return nextStep(train);
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
	public State nextStep(boolean train) {		
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
		if (train) {
			ArrayList<Double> des = model.getDesirabilitiesTrain(moves);
			
			// Generate random Double from 0 to 1
			Random r = new Random();
			Double rand = r.nextDouble();
			
			// Check in which range of accumulative list the random number falls
			int i = 0;
			double sum = 0.0;
			for (i = 0; i < des.size(); i++) {
				sum += des.get(i); // accumulate probabilities
				if (rand <= sum) {
					break;
				}
			}
			
			// Returns move with probability corresponding to desirability
			this.states.add(moves.get(i));
			
			return moves.get(i);
		} else {
			ArrayList<Double> des = model.getDesirabilities(moves);
			
			// get maximum desirability
			double max = des.get(0);
			int index = 0;
			for (int i = 0; i < des.size(); i++) {
				if (des.get(i) > max) {
					max = des.get(i);
					index = i;
				}
			}
			
			this.states.add(moves.get(index));
			
			return moves.get(index);
		}
	}
	
	public void resetGame() {
		this.states = new ArrayList<State>();
		this.states.add(new State(new Integer[] {0,0,0,0,0,0,0,0,0}));
	}
	
	public Model getModel() {
		return this.model;
	}

}
