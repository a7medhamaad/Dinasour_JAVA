package Dinasour;

import javax.swing.JFrame;

public class App {
	public static void main(String[] args) {
		int boardwidth = 750;
		int boardheight = 250;

		JFrame frame = new JFrame("Dinasour");
		frame.setSize(boardwidth, boardheight);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setVisible(true);

		ChromeDinasour chromeDinasour = new ChromeDinasour();
		frame.add(chromeDinasour);
		frame.pack();
		chromeDinasour.requestFocus();
		frame.setVisible(true);
	}

}
