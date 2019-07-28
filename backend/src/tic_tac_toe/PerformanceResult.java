package tic_tac_toe;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class PerformanceResult implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int win;
	private int lose;
	private int draw;
	private int trainingIterations;
	
	public PerformanceResult(int w, int l, int d) {
		setWin(w);
		setLose(l);
		setDraw(d);
	}
	
	public PerformanceResult() {}
	
	public void setWin(int w) {win = w;}
	public void setLose(int l) {lose = l;}
	public void setDraw(int d) {draw = d;}
	public void setTrainingIterations(int i) {trainingIterations = i;}
	
	public int getWin() {return win;}
	public int getLose() {return lose;}
	public int getDraw() {return draw;}
	public int getTotal() {return win + lose + draw;}
	public int getTrainingIterations() {return trainingIterations;}
	
	@Override
	public String toString() {
		return "Total: " + getTotal() + " Win: " + getWin() + " Lose: " + getLose() + " Draw: " + getDraw();
	}
	
}
