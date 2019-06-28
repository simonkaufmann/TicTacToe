import java.util.HashMap;

public class Model {
	
	HashMap<State, Float> desirability;
	int step;
	
	public Model(int step) {
		this.desirability = new HashMap<State, Float>();
		this.step = step;
	}
	
	public void increase(State s) {
		
	}
	
	public void decrease(State s) {
		
	}

}
