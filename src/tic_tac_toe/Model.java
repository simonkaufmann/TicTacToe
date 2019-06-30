package tic_tac_toe;
import java.util.HashMap;

import tic_tac_toe.Test_State;

public class Model {
	
	HashMap<State, Float> desirability;
	int step;
	
	public Model(int step) {
		this.desirability = new HashMap<State, Float>();
		this.step = step;
	}
	
	public void increase(Test_State s) {
		
	}
	
	public void decrease(Test_State s) {
		
	}

}
