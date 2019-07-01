package tic_tac_toe;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javax.swing.border.Border;

public class TicTacToe {
	
	public static void playGame(Game player1, Game player2) {
		State next = null;
		for (int i = 0; i < 5; i++) {
			next = player1.nextStep(next);
			next = player2.nextStep(next);
		}
	}
	
	public static void performanceTest(Game player1, String fn) {
		Model m = new Model();
		Game player = new Game(2, m);
		int won = 0, draw = 0, lost = 0;
		for (int i = 0; i < 100; i++) {
			playGame(player1, player);
			if (player1.result() == 'w') {
				won++;
			} else if (player1.result() == 'd') {
				draw++;
			} else if (player1.result() == 'l') {
				lost++;
			}
			player1.resetGame();
			player.resetGame();
		}
		System.out.println("\n\nPerformance Test:");
		System.out.println("Won: " + won + " Draw: " + draw + " Lost: " + lost);
		System.out.println("\n\n");
		
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

	public static void main(String[] args) {
		Model m1 = new Model();
		Model m2 = new Model();
		Game player1 = new Game(1, m1);
		Game player2 = new Game(2, m2);
		
		int iteration = 0;
//		while (true) {
//			System.out.println("Iteration " + iteration);
//			
//			for (int j = 0; j < 3000; j++) {
//				System.out.println("--" + Integer.toString(j) + "--");
//				playGame(player1, player2);
//				
//				player1.updateModel();
//				player2.updateModel();
//				
//				player1.resetGame();
//				player2.resetGame();
//			}
//			
//			performanceTest(player1, "performance_" + Integer.toString(iteration));
//			performanceTest(player1, "performance_" + Integer.toString(iteration));
//			performanceTest(player1, "performance_" + Integer.toString(iteration));
//			performanceTest(player1, "performance_" + Integer.toString(iteration));
//			performanceTest(player1, "performance_" + Integer.toString(iteration));
//			
//			player1.getModel().exportModel("player1_" + Integer.toString(iteration));
//			player2.getModel().exportModel("player2_" + Integer.toString(iteration));
//			
//			iteration++;
//		}
	}
	
}
