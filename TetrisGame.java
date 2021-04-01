package edu.mtc.egr283;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
public class TetrisGame extends JFrame {
	private static final long serialVersionUID = 1L;
	private JLabel statusBar;
	private Board board;
	public TetrisGame() {
		super();
		this.setSize(200, 400);
		this.setTitle("TetrisGame");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setLayout(new BorderLayout());
		this.statusBar = new JLabel(" 0");
		this.add(this.statusBar, BorderLayout.SOUTH);
		
		this.board = new Board(this);
		this.add(this.board);
		this.board.start();
	}// Ending bracket of constructor
	public JLabel getStatusBar() {
		return this.statusBar;
	}// Ending bracket of method getStatusBar
}// Ending bracket of class TetrisGame