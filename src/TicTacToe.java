import javax.swing.*;
import java.awt.*;
import javax.swing.border.Border;

public class TicTacToe {

	public static void main(String[] args) {
		System.out.println("Hello Tic Tac Toe");
		
		JFrame frame = new JFrame("Tic Tac Toe");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 400);
		frame.setVisible(true);
		
		JPanel pBoard = new JPanel();
		pBoard.setLayout(new GridLayout(3, 3));
		
		JPanel pCenter = new JPanel();
		pCenter.add(Box.createRigidArea(new Dimension(0, 20)));
		pCenter.add(pBoard);
		pCenter.setLayout(new BoxLayout(pCenter, BoxLayout.PAGE_AXIS));
		
		JPanel[][] pFields = new JPanel[3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				pFields[i][j]  = new JPanel();
				Border blackline = BorderFactory.createLineBorder(Color.black);
				pFields[i][j].setBorder(blackline);
				pBoard.add(pFields[i][j]);
			}
		}
		
		JButton btnReset = new JButton("Reset");
		JButton btnStart = new JButton("Start");
		
		JPanel pControls = new JPanel();
		FlowLayout flowControls = new FlowLayout();
		pControls.setLayout(flowControls);
		
		pControls.add(btnStart);
		pControls.add(btnReset);
		
		frame.add(pCenter);
		frame.add(pControls, BorderLayout.PAGE_END);
		
		pBoard.setMaximumSize(new Dimension(300,300));
	}
	
}
