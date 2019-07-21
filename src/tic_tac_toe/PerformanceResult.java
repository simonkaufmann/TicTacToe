package tic_tac_toe;

public class PerformanceResult {
	
	private int win;
	private int lose;
	private int draw;
	
	public PerformanceResult(int w, int l, int d) {
		setWin(w);
		setLose(l);
		setDraw(d);
	}
	
	public PerformanceResult() {}
	
	public void setWin(int w) {win = w;}
	public void setLose(int l) {lose = l;}
	public void setDraw(int d) {draw = d;}
	
	public int getWin() {return win;}
	public int getLose() {return lose;}
	public int getDraw() {return draw;}
	
}
