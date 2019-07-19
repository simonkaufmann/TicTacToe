package tic_tac_toe;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Model implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	
	HashMap<State, Double> vf; // value function for player X
	
	final double rewardWinX = 1;
	final double rewardWinO = -1;
	final double rewardTie = 0.5;
	final double rewardOther = 0;
	
	double alpha;
	State t_state;
	State t_state2;
	
	public Model() {
		this(0.1);
	}
	
	public Model(double alpha) {
		initialiseValueFunction();
		
		this.alpha = alpha;
	}
	
	private void initialiseValueFunction() {
		this.vf = new HashMap<State, Double>();

		int level = 0;
		Integer[] b = new Integer[9];
		initialiseVFRec(vf, level, b);
	}

	// Initialise value function recursively
	private void initialiseVFRec(HashMap<State, Double> vf, int level, Integer[] b) {
		if (level > 8) {
			return;
		}
		
		Integer[] b1 = b.clone();
		b1[level] = 0;
		
		Integer[] b2 = b.clone();
		b2[level] = 1;
		
		Integer[] b3 = b.clone();
		b3[level] = 2;
		
		if (level < 8) {
			initialiseVFRec(vf, level + 1, b1);
			initialiseVFRec(vf, level + 1, b2);
			initialiseVFRec(vf, level + 1, b3);
		} else {
			State s1 = new State(b1);
			State s2 = new State(b2);
			State s3 = new State(b3);

			vf.put(s1, 0.0);
			vf.put(s2, 0.0);
			vf.put(s3, 0.0);
		}
	}
	
	// Calculate next step according to model for player (either 1 or 2)
	// with training mode (sometimes exploration) or without (always choose optimal move)
	public State getNextStep(State s, int player, boolean training) {
		ArrayList<State> moves = s.nextSteps(player);
		
		// Abort if game is already decided
		if (s.result() != -1) {
			return s;
		}
		
		// no moves available
		if (moves.size() == 0) {
			return s;
		}
		
		if (Math.random() <= 0.2 && training) {
			// 0.2 probability for random move
			
			// Continue getting back best state and random state
			// dont forget checking in player that it is right turn
			// remove unnecessary functions here
		}
		
		return new State(new Integer[] {0,0,0,0,0,0,0,0});
	}
	
	public ArrayList<Double> getDesirabilitiesTrain(ArrayList<State> states) {
		// See Boltzman's distribution described here:
		// https://www.cs.dartmouth.edu/~lorenzo/teaching/cs134/Archive/Spring2009/final/PengTao/final_report.pdf
		
		ArrayList<Double> desirabilities = new ArrayList<Double>();
		Double sum = 0.0;
		
		for (int i = 0; i < states.size(); i++) {
			desirabilities.add(Math.exp(this.vf.get(states.get(i)) / temperature));
			sum+= desirabilities.get(i);
		}
		
		for (int i = 0; i < states.size(); i++) {
			desirabilities.set(i, Math.exp(desirabilities.get(i) / temperature) / sum);
		}
		
		return desirabilities;
	}
	
	public ArrayList<Double> getDesirabilities(ArrayList<State> states) {
		ArrayList<Double> stateValues = new ArrayList<Double>();
		
		for (int i = 0; i < states.size(); i++) {
			stateValues.add(this.vf.get(states.get(i)));
		}
		
		return stateValues;
	}
	
	public void exportModel(String fn) {
        try {
			FileOutputStream fos =
		        new FileOutputStream(fn);
		    ObjectOutputStream oos = new ObjectOutputStream(fos);
		    oos.writeObject(this);
		    oos.close();
		    fos.close();
        } catch(IOException ioe) {
        	ioe.printStackTrace();
        }
	}
	
	private void updateModel(ArrayList<State> states, double reward) {
		// Best this.vf for end state
		this.vf.put(states.get(states.size() - 1), reward);
		
		double des_after = reward;
		for (int i = states.size() - 2; i >= 0; i--) {
			double des_current = this.vf.get(states.get(i));
			des_current = (1 - alpha()) * des_current + alpha() * (des_after - des_current);
			this.vf.put(states.get(i), des_current);
			des_after = des_current;
		}
		
		rounds++;
	}
	
	private double alpha() {
		//return base_alpha * 1 / rounds;
		return alpha;
	}
	
	public void updateModelWin(ArrayList<State> states) {
		updateModel(states, reward);
	}
	
	public void updateModelLose(ArrayList<State> states) {
		updateModel(states, -reward);
	}
	
	@SuppressWarnings("unchecked")
	private void readObject(ObjectInputStream aInputStream) throws ClassNotFoundException, IOException
    {      
		this.vf = (HashMap<State, Double>) aInputStream.readObject();
		alpha = aInputStream.readDouble();
    }
 
    private void writeObject(ObjectOutputStream aOutputStream) throws IOException
    {
    	aOutputStream.writeObject(this.vf);
    	aOutputStream.writeDouble(alpha);
    }
}
