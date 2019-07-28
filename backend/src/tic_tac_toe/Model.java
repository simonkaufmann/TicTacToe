package tic_tac_toe;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
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
	ArrayList<PerformanceResult> performanceX;
	ArrayList<PerformanceResult> performanceO;
	
	final double rewardWinX = 1;
	final double rewardWinO = -1;
	final double rewardTie = 0.5;
	final double rewardOther = 0;
	
	double alpha;
	double chanceRandomMove;
	int trainingIterations;
	
	public Model() {
		this(0.1, 0.2);
	}
	
	public Model(double alpha, double randomMove) {
		initialiseValueFunction();
		
		this.performanceX = new ArrayList<PerformanceResult>();
		this.performanceO = new ArrayList<PerformanceResult>();
		this.alpha = alpha;
		this.trainingIterations = 0;
		this.chanceRandomMove = randomMove;
	}
	
	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}
	
	public double getAlpha() {
		return this.alpha;
	}
	
	public void setChanceRandomMove(double randomMove) {
		this.chanceRandomMove = randomMove;
	}
	
	public double getChanceRandomMove() {
		return this.chanceRandomMove;
	}
	
	private void initialiseValueFunction() {
		this.vf = new HashMap<State, Double>();

		int level = 0;
		Integer[] b = new Integer[9];
		initialiseVFRec(vf, level, b);
	}

	private void putState(State s) {
//		if (s.result() == State.WIN_PLAYER_X) {
//			this.vf.put(s, this.rewardWinX);
//		} else if (s.result() == State.WIN_PLAYER_O) {
//			this.vf.put(s, this.rewardWinO);
//		} else if (s.result() == State.DRAW) {
//			this.vf.put(s, this.rewardTie);
//		} else {
//			this.vf.put(s, this.rewardOther);
//		}
		this.vf.put(s, this.rewardOther);
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

			this.putState(s1);
			this.putState(s2);
			this.putState(s3);
		}
	}
	
	private int indexMax(ArrayList<Double> l) {
		double max = Double.NEGATIVE_INFINITY;
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
		double min = Double.POSITIVE_INFINITY;
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
			return Double.POSITIVE_INFINITY;
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
			return Double.NEGATIVE_INFINITY;
		}
		
		double v = this.vf.get(moves.get(0));
		for (State s: moves) {
			if (this.vf.get(s) > v) {
				v = this.vf.get(s);
			}
		}
		return v;
	}
	
	private State getRandomNextMove(State s, int player) {
		ArrayList<State> moves = s.nextMoves(player);
		Random rn = new Random();
		int index = rn.nextInt(moves.size());
		return moves.get(index);
	}
	
	/*
	 *  Calculate next step according to model for player (either 1 or 2)
	 * with training mode (sometimes exploration) or without (always choose optimal move)
	 */
	public State getNextMove(State s, int player, boolean training) {
		ArrayList<State> moves = s.nextMoves(player);
		
		Function<ArrayList<State>, Double> bestOther;
		Function<ArrayList<Double>, Integer> bestPlayer;
		int otherPlayer;
		
		if (player == State.PLAYER_X) {
			// player is X
			bestOther = this::minValue;
			bestPlayer = this::indexMax;
			otherPlayer = State.PLAYER_O;
		} else {
			bestOther = this::maxValue;
			bestPlayer = this::indexMin;
			otherPlayer = State.PLAYER_X;
		}
		
		// Abort if game is already decided
		if (s.result() != -1) {
			return s;
		}
		
		// no moves available
		if (moves.size() == 0) {
			return s;
		}
		
		// probability for random move
		if (Math.random() <= this.chanceRandomMove && training) {
			return getRandomNextMove(s, player);
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
	
	// Plays one game training itself
	private void trainingGame() {
		Game game = new Game();
		
		int player = State.PLAYER_X;
		
		while (game.result() == State.UNDECIDED) {
			State lastMove = game.getMove();
			State nextMove = this.getNextMove(lastMove, player, true);
			game.addMove(nextMove);
			player = State.switchPlayer(player);
			
			// Update value function
			double lastVf = vf.get(lastMove);
			double nextVf = vf.get(nextMove);
			double reward = this.rewardOther;
			if (nextMove.result() == State.WIN_PLAYER_X) {
				reward = this.rewardWinX;
			} else if (nextMove.result() == State.WIN_PLAYER_O) {
				reward = this.rewardWinO;
			} else if (nextMove.result() == State.DRAW) {
				reward = this.rewardTie;
			}
			lastVf = lastVf + alpha * (nextVf + reward - lastVf);
			vf.put(lastMove, lastVf);
		}
		
		trainingIterations++;
	}
	
	public void trainModel(int iterations) {
		for (int i = 0; i < iterations; i++) {
			this.trainingGame();
		}
	}
	
	/* Play one game to test performance against random player
	 * Argument which player should be taken from model
	 * Returns result of game
	 */
	private int performanceGame(int player) {
		Game game = new Game();
		
		int p = State.PLAYER_X;
		
		while(game.result() == State.UNDECIDED) {
			if (p == player) {
				State last = game.getMove();
				game.addMove(this.getNextMove(last, p, false));
			} else {
				State last = game.getMove();
				game.addMove(this.getRandomNextMove(last, p));
			}
			p = State.switchPlayer(p);
		}
		return game.result();
	}
	
	/* Test performance through multiple games
	 * player is player taken from model, other player is random
	 */
	public PerformanceResult testPerformance(int iterations, int player) {
		PerformanceResult result = new PerformanceResult();
		
		for (int i = 0; i < iterations; i++) {
			int res = performanceGame(player);
			
			if (res == State.DRAW) {
				result.setDraw(result.getDraw() + 1);
			} else if (res == player) {
				result.setWin(result.getWin() + 1);
			} else {
				result.setLose(result.getLose() + 1);
			}
		}
		
		result.setTrainingIterations(this.trainingIterations);
		
		if (player == State.PLAYER_X) {
			this.performanceX.add(result);
		} else {
			this.performanceO.add(result);
		}
		
		return result;
	}
	
	public ArrayList<PerformanceResult> getPerformance(int player) {
		if (player == State.PLAYER_X) {
			return this.performanceX;
		} else {
			return this.performanceO;
		}
	}
	
	public byte[] exportModelToByte() {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(this);
			oos.flush();
			return bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
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
	
	public static Model importModel(String fn) {
		try {
			FileInputStream fis =
				new FileInputStream(fn);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Model m = new Model();
			m = (Model) ois.readObject();		
			ois.close();
			fis.close();
			return m;
		} catch(IOException ioe) {
			ioe.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private void readObject(ObjectInputStream aInputStream) throws ClassNotFoundException, IOException
    {      
		this.vf = new HashMap<State, Double>();
		int size = aInputStream.readInt();
		for (int i = 0; i < size; i++) {
			Integer[] arr = new Integer[9];
			for (int j = 0; j < 9; j++) {
				arr[j] = aInputStream.readInt();
			}
			State s = new State(arr);
			double d = aInputStream.readDouble();
			this.vf.put(s, d);
		}
		
		this.performanceX = (ArrayList<PerformanceResult>) aInputStream.readObject();
		this.performanceO = (ArrayList<PerformanceResult>) aInputStream.readObject();
		this.alpha = aInputStream.readDouble();
		this.trainingIterations = aInputStream.readInt();
    }
 
    private void writeObject(ObjectOutputStream aOutputStream) throws IOException
    {
    	int size = this.vf.size();
    	aOutputStream.writeInt(size);

    	// Needs to be serialized in this complicated way:
    	// https://www.thecodingforums.com/threads/deserialization-bug-nullpointerexception-thrown-during-hashmap-hash.147629/
    	// Reason: With desirialization hashCode of State seems to not work properly
    	// if the object hasn't been fully deserialized and initialized while reading it
    	for (State s: this.vf.keySet()) {
    		Integer[] i = s.get();
    		for (int j = 0; j < 9; j++) {
    			aOutputStream.writeInt(i[j]);
    		}
    		aOutputStream.writeDouble(this.vf.get(s));
    	}
    	
    	aOutputStream.writeObject(this.performanceX);
    	aOutputStream.writeObject(this.performanceO);
    	aOutputStream.writeDouble(alpha);
    	aOutputStream.writeInt(this.trainingIterations);
    }
}
