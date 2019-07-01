package tic_tac_toe;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import tic_tac_toe.Test_State;

public class Model implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	
	HashMap<State, Double> desirability;
	
	double reward;
	double base_alpha;
	double temperature;
	double rounds;
	State t_state;
	State t_state2;
	
	public Model() {
		this(0.1, 1);
	}
	
	public Model(double alpha, double temperature) {
		initialiseDesirability();
		
		this.reward = 1;
		this.base_alpha = alpha;
		this.temperature = temperature;
		this.rounds = 1;
	}
	
	private void initialiseDesirability() {
		desirability = new HashMap<State, Double>();

		int level = 0;
		Integer[] b = new Integer[9];
		initialiseDesRec(level, b);
	}

	private void initialiseDesRec(int level, Integer[] b) {
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
			initialiseDesRec(level + 1, b1);
			initialiseDesRec(level + 1, b2);
			initialiseDesRec(level + 1, b3);
		} else {
			State s1 = new State(b1);
			State s2 = new State(b2);
			State s3 = new State(b3);

			desirability.put(s1, 0.0);
			desirability.put(s2, 0.0);
			desirability.put(s3, 0.0);
		}
	}
	
	public ArrayList<Double> getDesirabilitiesTrain(ArrayList<State> states) {
		// See Boltzman's distribution described here:
		// https://www.cs.dartmouth.edu/~lorenzo/teaching/cs134/Archive/Spring2009/final/PengTao/final_report.pdf
		
		ArrayList<Double> desirabilities = new ArrayList<Double>();
		Double sum = 0.0;
		
		for (int i = 0; i < states.size(); i++) {
			desirabilities.add(Math.exp(desirability.get(states.get(i)) / temperature));
			sum+= desirabilities.get(i);
		}
		
		for (int i = 0; i < states.size(); i++) {
			desirabilities.set(i, Math.exp(desirabilities.get(i) / temperature) / sum);
		}
		
		return desirabilities;
	}
	
	public ArrayList<Double> getDesirabilities(ArrayList<State> states) {
		ArrayList<Double> desirabilities = new ArrayList<Double>();
		
		for (int i = 0; i < states.size(); i++) {
			desirabilities.add(desirability.get(states.get(i)));
		}
		
		return desirabilities;
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
		// Best desirability for end state
		desirability.put(states.get(states.size() - 1), reward);
		
		double des_after = reward;
		for (int i = states.size() - 2; i >= 0; i--) {
			double des_current = desirability.get(states.get(i));
			des_current = (1 - alpha()) * des_current + alpha() * (des_after - des_current);
			desirability.put(states.get(i), des_current);
			des_after = des_current;
		}
		
		rounds++;
	}
	
	private double alpha() {
		//return base_alpha * 1 / rounds;
		return base_alpha;
	}
	
	public void updateModelWin(ArrayList<State> states) {
		updateModel(states, reward);
	}
	
	public void updateModelLose(ArrayList<State> states) {
		updateModel(states, -reward);
	}
	
	private void readObject(ObjectInputStream aInputStream) throws ClassNotFoundException, IOException
    {      
		this.desirability = (HashMap<State, Double>) aInputStream.readObject();
		reward = aInputStream.readDouble();
		base_alpha = aInputStream.readDouble();
		temperature = aInputStream.readDouble();
		rounds = aInputStream.readDouble();
    }
 
    private void writeObject(ObjectOutputStream aOutputStream) throws IOException
    {
    	aOutputStream.writeObject(this.desirability);
    	aOutputStream.writeDouble(reward);
    	aOutputStream.writeDouble(base_alpha);
    	aOutputStream.writeDouble(temperature);
    	aOutputStream.writeDouble(rounds);
    }
}
