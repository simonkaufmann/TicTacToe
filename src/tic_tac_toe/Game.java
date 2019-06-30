package tic_tac_toe;
import java.util.ArrayList;

import tic_tac_toe.Test_State;

public class Game {
	
	int player;
	Model model;
	ArrayList<Test_State> states;
	
	public Game(int player, Model m) {
		this.player = player;
		this.model = m;
		this.states = new ArrayList<Test_State>();
	}
	
	public boolean otherPlayer(int field) {
		return true;
	}
	
	public boolean nextStep() {
		return true;
	}

}
