package tic_tac_toe;

import java.io.FileOutputStream;
import java.io.IOException;

public class Training {
	
	public static void playGame(boolean train1, Player player1, boolean train2, Player player2) {
		State next = null;
		for (int i = 0; i < 5; i++) {
			next = player1.nextStep(train1, next);
			next = player2.nextStep(train2, next);
		}
	}
	
	public static void performanceTest(Player player2, String fn) {
		Model m = new Model();
		Player player = new Player(1, m);
		int won = 0, draw = 0, lost = 0;
		for (int i = 0; i < 100; i++) {
			playGame(true, player, false, player2);
			if (player2.result() == 'w') {
				won++;
			} else if (player2.result() == 'd') {
				draw++;
			} else if (player2.result() == 'l') {
				lost++;
			}
			player2.resetGame();
			player.resetGame();
		}
		System.out.println("Performance Test:");
		System.out.println("Won: " + won + " Draw: " + draw + " Lost: " + lost);
		System.out.println("");
		
		try {
			String str = "Won: " + won + " Draw: " + draw + " Lost: " + lost;
	
		    FileOutputStream outputStream = new FileOutputStream(fn);
		    byte[] strToBytes = str.getBytes();
		    outputStream.write(strToBytes);
		 
		    outputStream.close();
		} catch(IOException ioe)
        {
            ioe.printStackTrace();
        }
	}
	
	public static void main_old() {
		Model m1 = new Model();
		Model m2 = new Model();
		Player player1 = new Player(1, m1);
		Player player2 = new Player(2, m2);
		
		int iteration = 0;
		while (true) {
			System.out.println("Iteration " + iteration);
			
			performanceTest(player2, "performance_" + Integer.toString(iteration) + "_1");
			performanceTest(player2, "performance_" + Integer.toString(iteration) + "_2");
			performanceTest(player2, "performance_" + Integer.toString(iteration) + "_3");
			performanceTest(player2, "performance_" + Integer.toString(iteration) + "_4");
			performanceTest(player2, "performance_" + Integer.toString(iteration) + "_5");
			
			for (int j = 0; j < 10000000; j++) {
				if (j % 100000 == 0) {
					System.out.println("--" + Integer.toString(j) + "--");
				}
				playGame(true, player1, true, player2);
				
				player1.updateModel();
				player2.updateModel();
				
				player1.resetGame();
				player2.resetGame();
			}
			
			player1.getModel().exportModel("player1_" + Integer.toString(iteration));
			player2.getModel().exportModel("player2_" + Integer.toString(iteration));
			
			iteration++;
		}
	}
	
}
