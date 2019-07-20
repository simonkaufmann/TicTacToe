package tic_tac_toe;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.function.Function;

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
	
	private int indexMax(ArrayList<Double> l) {
		double max = Double.MIN_VALUE;
		int index = -1;
		for (int i = 0; i < l.size(); i++) {
			if (l.get(i) > max) {
				max = l.get(i);
				index = i;
			}
		}
		return index;
	}
	
	private Integer indexMin(ArrayList<Double> l) {
		double min = Double.MAX_VALUE;
		int index = -1;
		for (int i = 0; i < l.size(); i++) {
			if (l.get(i) < min) {
				min = l.get(i);
				index = i;
			}
		}
		return index;
	}
	
	private Double minValue(ArrayList<State> moves) {
		if (moves.size() == 0) {
			return Double.MAX_VALUE;
		}

		double v = this.vf.get(moves.get(0));
		for (State s: moves) {
			if (this.vf.get(s) < v) {
				v = this.vf.get(s);
			}
		}
		return v;
	}
	
	private Double maxValue(ArrayList<State> moves) {
		if (moves.size() == 0) {
			return Double.MIN_VALUE;
		}

		double v = this.vf.get(moves.get(0));
		for (State s: moves) {
			if (this.vf.get(s) > v) {
				v = this.vf.get(s);
			}
		}
		return v;
	}
	
	// Calculate next step according to model for player (either 1 or 2)
	// with training mode (sometimes exploration) or without (always choose optimal move)
	public State getNextMove(State s, int player, boolean training) {
		ArrayList<State> moves = s.nextMoves(player);
		
		Function<ArrayList<State>, Double> bestOther;
		Function<ArrayList<Double>, Integer> bestPlayer;
		int otherPlayer;
		
		if (player == 1) {
			// player is X
			bestOther = this::minValue;
			bestPlayer = this::indexMax;
			otherPlayer = 2;
		} else {
			bestOther = this::maxValue;
			bestPlayer = this::indexMin;
			otherPlayer = 1;
		}
		
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
			
			Random rn = new Random();
			int index = rn.nextInt() % (moves.size() + 1);
			return moves.get(index);
		}
		
		// For every move of player calculate result of assumed optimal other player
		ArrayList<Double> valMoves = new ArrayList<Double>();
		for (State state: moves) {
			ArrayList<State> next = state.nextMoves(otherPlayer);
			valMoves.add(bestOther.apply(next));
		}
		
		// Take the best result of results of other player
		int indexBest = bestPlayer.apply(valMoves);
		
		return moves.get(indexBest);		
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
