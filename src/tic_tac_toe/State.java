package tic_tac_toe;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import tic_tac_toe.State;

public class State implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final int PLAYER_X = 1;
	public static final int PLAYER_O = 2;
	public static final int EMPTY = 0;
	public static final int DRAW = 0;
	public static final int UNDECIDED = -1;
	
	private Integer[] board;
	
	public State(Integer[] board) {
		this.board = board;
	}
	
	public ArrayList<State> nextMoves(int player) {
		ArrayList<State> next = new ArrayList<State>();
		
		for (int i = 0; i < board.length; i++) {
			if (board[i] == 0) {
				Integer[] new_state = board.clone();
				new_state[i] = player;
				next.add(new State(new_state));
			}
		}
		
		return next;
	}
	
	private boolean win(int player) {
		for (int i = 0; i < 3; i++) {
			// horizontal:
			if (board[3 * i + 0] == player &&
				board[3 * i + 1] == player &&
				board[3 * i + 2] == player) {
				return true;
			}
			// vertical:
			if (board[0 + i] == player &&
				board[3 + i] == player &&
				board[6 + i] == player) {
				return true;
			}
		}
		// diagonal:
		if (board[0] == player &&
			board[4] == player &&
			board[8] == player) {
			return true;
		}
		if (board[2] == player &&
			board[4] == player &&
			board[6] == player) {
			return true;
		}
		return false;
	}
	
	
	// -1: not finished, 0: draw, 1: player 1 (X) wins, 2: player 2 (O) wins
	public int result() {
		int res = State.DRAW; // draw
		
		for (int i = 0; i < 9; i++) {
			if (board[i] == State.EMPTY) {
				res = State.UNDECIDED; // not decided yet
			}
		}
		
		if (win(State.PLAYER_X))
			res = State.PLAYER_X; // player 1 wins
		else if (win(State.PLAYER_O))
			res = State.PLAYER_O; // player 2 wins
		
		return res;
	}
	
	public Integer[] get() {
		return this.board;
	}
	
	@Override
	public boolean equals(Object o) {
		State s = (State) o;
		
		boolean equals = true;
 
		if (s == null || s.board == null || s.board.length != 9) {
			return false;
		}
		
		for (int i = 0; i < 9; i++) {
			if (board[i] != s.board[i]) {
				equals = false;
			}
		}
		
		return equals;	
	}

	@Override
	public int hashCode() {
		int hash = 0;
		for (int i = 0; i < 9; i++) {
			hash += (int) Math.pow(3, i) * board[i];
		}
		return hash;
	}
	
	@Override
	public String toString() {
		Character[] c = new Character[9];
		for (int i = 0; i < 9; i++) {
			if (board[i] == State.PLAYER_X) {
				c[i] = 'X';
			} else if (board[i] == State.PLAYER_O) {
				c[i] = 'O';
			} else {
				c[i] = ' ';
			}
		}
		String out = "-------\n" +
				 "|" + c[0] + " " + c[1] + " " + c[2] + "|\n" +
				 "|" + c[3] + " " + c[4] + " " + c[5] + "|\n" +
				 "|" + c[6] + " " + c[7] + " " + c[8] + "|\n" +
				 "-------";
		
		return out;
	}
	
	private void readObject(ObjectInputStream aInputStream) throws ClassNotFoundException, IOException
    {      
		this.board = (Integer[]) aInputStream.readObject();
    }
 
    private void writeObject(ObjectOutputStream aOutputStream) throws IOException
    {
    	aOutputStream.writeObject(this.board);
    }
}
