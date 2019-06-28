import java.util.ArrayList;

public class Game {
	
	int player;
	Model model;
	ArrayList<State> states;
	
	public Game(int player, Model m) {
		this.player = player;
		this.model = m;
		this.states = new ArrayList<State>();
	}
	
	public boolean otherPlayer(int field) {
		return true;
	}
	
	public boolean nextStep() {
		return true;
	}

}
