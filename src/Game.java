
public class Game {
	
	int player;
	Model model;
	
	public Game(int player, Model m) {
		this.player = player;
		this.model = m;
	}
	
	public boolean otherPlayer(int field) {
		return true;
	}
	
	public boolean nextStep() {
		return true;
	}

}
