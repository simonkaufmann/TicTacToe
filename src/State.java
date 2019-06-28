import java.util.ArrayList;

public class State {
	
	private Integer[] board;
	
	public State(Integer[] board) {
		this.board = board;
	}
	
	public ArrayList<State> nextSteps(int player) {
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
	
	public int result() {
		int res = 0; // draw
		
		for (int i = 0; i < 9; i++) {
			if (board[i] == 0) {
				res = -1; // not decided yet
			}
		}
		
		if (win(1))
			res = 1; // player 1 wins
		else if (win(2))
			res = 2; // player 2 wins
		
		return res;
	}
	
	public Integer[] get() {
		return this.board;
	}

}
