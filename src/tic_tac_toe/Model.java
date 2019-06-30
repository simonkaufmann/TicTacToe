package tic_tac_toe;
import java.util.HashMap;

import tic_tac_toe.Test_State;

public class Model {
	
	HashMap<State, Double> desirability;
	int step;
	
	public Model(int step) {
		this.desirability = new HashMap<State, Double>();
		this.step = step;
	}
	
	public void increase(Test_State s) {
		
	}
	
	public void decrease(Test_State s) {
		
	}

	public Double getDesirability(State s) {
		return desirability.get(s);
	}
	
	public void updateModel(State s, int past_steps) {
		
	}
}
