package tic_tac_toe;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.border.Border;

public class TicTacToe {

	public static void main(String[] args) {
		Model m1 = new Model(0.1);
		Model m2 = new Model(0.1);
		Game player1 = new Game(1, m1);
		Game player2 = new Game(2, m2);
		
		State next = null;
		for (int i = 0; i < 5; i++) {
			next = player1.nextStep(next);
			next = player2.nextStep(next);
		}
		
		ArrayList<State> s1 = player1.getStates();
		ArrayList<State> s2 = player2.getStates();
		
		System.out.println("\n\nPlayer 1:");
		for(State s: s1) {
			System.out.println(s);
		}
		System.out.println("\n\nPlayer 2:");
		for(State s: s2) {
			System.out.println(s);
		}
	}
	
}
