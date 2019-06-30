package tic_tac_toe;
import java.util.ArrayList;
import java.util.Collection;
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
		if (desirability.containsKey(s)) {
			return desirability.get(s);
		} else {
			// Return average of hashmap
			Collection<Double> val = desirability.values();
			double sum = 0.0;
			for (Double v: val) {
				sum += v;
			}
			return sum / val.size();
		}
	}
	
	public void updateModel(ArrayList<State> s) {
		
	}
}
