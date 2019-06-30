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
	private static final long serialVersionUID = 1L;
	
	HashMap<State, Double> desirability;
	double step;
	double average;
	double min;
	double max;
	
	public Model(double step) {
		this.desirability = new HashMap<State, Double>();
		this.step = step;
		
		update();
	}
	
	private void update() {
		// Calculate average, min, max
		Collection<Double> val = this.desirability.values();
		if (val.size() == 0) {
			this.average = 0.5;
			this.min = 0;
			this.max = 1;
		} else {
			this.min = Collections.min(val);
			this.max = Collections.max(val);
			
			double sum = 0.0;
			for (Double v: val) {
				if (v < this.min) {
					this.min = v;
				}
				if (v > this.max) {
					this.max = v;
				}
				sum += v;
			}
			this.average = sum / val.size();
		}
	}
	
	public void increase(State s) {
		double val = 0.0;
		if (desirability.containsKey(s)) {
			val = desirability.get(s);
		}
		val += step;
		desirability.put(s, val);
		
		update();
	}
	
	public void decrease(State s) {
		double val = 0.0;
		if (desirability.containsKey(s)) {
			val = desirability.get(s);
		}
		val -= step;
		desirability.put(s, val);
		
		update();
	}

	public Double getDesirability(State s) {
		if (desirability.containsKey(s)) {
			return desirability.get(s) + this.min;
		} else {
			return this.average;
		}
	}
	
	public void exportModel(String fn) {
        try {
		FileOutputStream fos =
	        new FileOutputStream(fn);
	    ObjectOutputStream oos = new ObjectOutputStream(fos);
	    oos.writeObject(this);
	    oos.close();
	    fos.close();
	    System.out.printf("Serialized HashMap data is saved in hashmap.ser");
        } catch(IOException ioe)
        {
                  ioe.printStackTrace();
        }
	}
	
	public void updateModelWin(ArrayList<State> states) {
		for (State s: states) {
			this.increase(s);
		}
	}
	
	public void updateModelLose(ArrayList<State> states) {
		for (State s: states) {
			this.decrease(s);
		}
	}
	
	private void readObject(ObjectInputStream aInputStream) throws ClassNotFoundException, IOException
    {      
		this.desirability = (HashMap<State, Double>) aInputStream.readObject();
		this.step = aInputStream.readDouble();
		this.average = aInputStream.readDouble();
		this.min = aInputStream.readDouble();
		this.max = aInputStream.readDouble();
    }
 
    private void writeObject(ObjectOutputStream aOutputStream) throws IOException
    {
    	aOutputStream.writeObject(this.desirability);
    	aOutputStream.writeDouble(step);
    	aOutputStream.writeDouble(average);
    	aOutputStream.writeDouble(min);
    	aOutputStream.writeDouble(max);
    }
}
