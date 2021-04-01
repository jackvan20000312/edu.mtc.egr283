package edu.mtc.egr283;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static final int BOARDWIDTH = 10;
	private static final int BOARDHEIGHT = 22;

	private Timer timer;
	private boolean isFallingFinished;
	private boolean isStarted;
	private boolean isPaused;
	private int numLinesRemoved;
	private int curX;
	private int curY;
	private JLabel statusBar;
	private Shape curPiece;
	private Tetrominoes[] board;

	public Board(TetrisGame parent) {
		super();
		this.isFallingFinished = false;
		this.isStarted = false;
		this.isPaused = false;
		this.numLinesRemoved = 0;
		this.curX = 0;
		this.curY = 0;

		this.setFocusable(true);
		this.curPiece = new Shape();
		this.timer = new Timer(400, this);
		this.timer.start();
		this.statusBar = parent.getStatusBar();
		this.board = new Tetrominoes[Board.BOARDHEIGHT * Board.BOARDWIDTH];
		this.addKeyListener(new TAdapter());
		this.clearBoard();
	}// Ending bracket of constructor

	public void actionPerformed(ActionEvent ae) {
		if(this.isFallingFinished) {
			this.isFallingFinished = false;
			this.newPiece();
			} else {
			this.oneLineDown();
			}// Ending bracket of if
	}// Ending bracket of method actionPerformed

	private int squareWidth() {
		return (int) this.getSize().getWidth() / Board.BOARDWIDTH;
	}// Ending bracket of method squareWidth

	private int squareHeight() {
		return (int) this.getSize().getHeight() / Board.BOARDHEIGHT;
	}// Ending bracket of method squareHeight

	private Tetrominoes shapeAt(int x, int y) {
		return this.board[(y * Board.BOARDWIDTH) + x];
	}// Ending bracket of method shapeAt

	public void start() {
		if(this.isPaused) {
			return;
		}// Ending bracket of if
		this.isStarted = true;
		this.isFallingFinished = false;
		this.numLinesRemoved = 0;
		this.clearBoard();
		this.newPiece();
		this.timer.start();
	}// Ending bracket of method start

	private void pause() {
		if(!this.isStarted) {
			return;
		}// Ending bracket of if
		this.isPaused = !this.isPaused;
		if(this.isPaused) {
			this.timer.stop();
			this.statusBar.setText(" -— paused ——");
		} else {
			this.timer.start();
			this.statusBar.setText(String.valueOf(this.numLinesRemoved));
		}// Ending bracket of if
		this.repaint();
	}// Ending bracket of method pause

	private boolean tryMove(Shape newPiece, int newX, int newY) {
		for(int i = 0; i < 4; ++i) {
			int x = newX + newPiece.x(i);
			int y = newY - newPiece.y(i);
			if(x < 0 || x >= Board.BOARDWIDTH || y < 0 || y >= Board.BOARDHEIGHT) {
				return false;
			}// Ending bracket of if
			if(shapeAt(x,y) != Tetrominoes.NoShape) {
				return false;
			}// Ending bracket of if
		}// Ending bracket of for loop
		this.curPiece = newPiece;
		this.curX = newX;
		this.curY = newY;
		this.repaint();
		return true;
	}// Ending bracket of method tryMove

	public void paint(Graphics g) {
		super.paint(g);
		Dimension size = this.getSize();
		int boardTop = (int) size.getHeight() - Board.BOARDHEIGHT * this.squareHeight();
		Tetrominoes shape;
		for(int i = 0; i < Board.BOARDHEIGHT; ++i) {
			for(int j = 0; j < Board.BOARDWIDTH; ++j) {
				shape = this.shapeAt(j, Board.BOARDHEIGHT - i - 1);
				if(shape != Tetrominoes.NoShape) {
					this.drawSquare(g, 0 + j * this.squareWidth(), boardTop + i * this.squareHeight(), shape);
				}// Ending bracket of if
			}// Ending bracket of INNER for loop
		}// Ending bracket of OUTER for loop
		if(this.curPiece.getShape() != Tetrominoes.NoShape) {
			for(int i = 0; i < 4; ++i) {
				int x = this.curX + this.curPiece.x(i);
				int y = this.curY - this.curPiece.y(i);
				this.drawSquare(g, 0 + x * this.squareWidth(),
						boardTop + (Board.BOARDHEIGHT - y - 1) * this.squareHeight(), this.curPiece.getShape());
			}// Ending bracket of for loop
		}// Ending bracket of if
	}// Ending bracket of method paint

	private void dropDown() {
		int newY = this.curY;
		while(newY > 0) {
			if(!this.tryMove(this.curPiece, this.curX, newY - 1)) {
				break;
			}// Ending bracket of if
			--newY;
		}// Ending bracket of while loop
		this.pieceDropped();
	}// Ending bracket of method dropDown
	
	private void oneLineDown() {
		if(!this.tryMove(this.curPiece, this.curX, this.curY - 1)) {
			this.pieceDropped();
		}// Ending bracket of if
	}// Ending bracket of method oneLineDown
	
	private void clearBoard() {
		for(int i = 0; i < Board.BOARDHEIGHT * Board.BOARDWIDTH; ++i) {
			this.board[i] = Tetrominoes.NoShape;
		}// Ending bracket of for loop
	}// Ending bracket of method clearBoard

	private void pieceDropped() {
		for(int i = 0; i < 4; ++i) {
			int x = this.curX + this.curPiece.x(i);
			int y = this.curY - this.curPiece.y(i);
			this.board[(y * Board.BOARDWIDTH) + x] = this.curPiece.getShape();
		}// Ending bracket of for loop
		this.removeFullLines();
		if(!this.isFallingFinished) {
			this.newPiece();
		}// Ending bracket of if
	}// Ending bracket of method pieceDrop

	private void newPiece() {
		this.curPiece.setRandomShape();
		this.curX = (Board.BOARDWIDTH / 2) + 1;
		this.curY = Board.BOARDHEIGHT - 1 + this.curPiece.minY();
		if(!tryMove(this.curPiece, this.curX, this.curY)) {
			this.curPiece.setShape(Tetrominoes.NoShape);
			this.timer.stop();
			this.isStarted = false;
			this.statusBar.setText(this.numLinesRemoved + " — GAME OVER!");
		}// Ending bracket of if
	}// Ending bracket of method newPiece

	private void removeFullLines() {
		int numFullLines = 0;

		boolean lineIsFull;
		for(int i = Board.BOARDHEIGHT - 1; i >= 0; --i) {
			lineIsFull = true;

			for(int j = 0; j < Board.BOARDWIDTH; ++j) {
				if(this.shapeAt(j, 1) == Tetrominoes.NoShape) {
					lineIsFull = false;
					break;
				}// Ending bracket of if
			}// Ending bracket of INNER for loop

			if(lineIsFull) {
				++numFullLines;
				for(int k = i; k < Board.BOARDHEIGHT - 1; ++k) {
					for(int j = 0; j < Board.BOARDWIDTH; ++j) {
						this.board[(k * Board.BOARDWIDTH) + j] = this.shapeAt(j, k + 1);
					}// Ending bracket of third INNER for loop
				}// Ending bracket of INNER for loop
			}// Ending bracket of if
		}// Ending bracket of OUTER for loop

		if(numFullLines > 0) {
			this.numLinesRemoved += numFullLines;
			this.statusBar.setText(String.valueOf(numLinesRemoved));
			this.isFallingFinished = true;
			this.curPiece.setShape(Tetrominoes.NoShape);
			this.repaint();
		}// Ending bracket of if
	}// Ending bracket of method removeFullLines

	private void drawSquare(Graphics g, int x, int y, Tetrominoes shape) {
		Color[] colors = {	new Color(0, 0, 0), new Color(204, 102, 102), new Color(102, 204, 102),
				new Color(102, 102, 204), new Color(204, 204, 102), new Color(204, 102, 204),
				new Color(102, 204, 204), new Color(218, 170, 0)};
		Color color = colors[shape.ordinal()];
		g.setColor(color);
		g.fillRect(x + 1, y + 1, this.squareWidth() - 2, this.squareHeight() - 2);

		g.setColor(color.brighter());
		g.drawLine(x, y + this.squareHeight() - 1, x, y);
		g.drawLine(x, y, x + this.squareWidth() - 1, y);

		g.setColor(color.darker());
		g.drawLine(x + 1, y + this.squareHeight() - 1, x + this.squareWidth() - 1, y + this.squareHeight() - 1);
		g.drawLine(x + this.squareWidth() - 1, y + this.squareHeight() - 1, x + this.squareWidth() - 1, y + 1);
	}// Endinq bracket of method drawSouare

	class TAdapter extends KeyAdapter {
		public void keyPressed(KeyEvent ke) {
			if(!isStarted || curPiece.getShape() == Tetrominoes.NoShape) {
				return;
			}// Ending bracket of if
			int keycode = ke.getKeyCode();
			if(keycode == 'p' || keycode == 'P') {
				pause();
				return;
			}// Ending bracket of if
			if(isPaused) {
				return;
			}// Ending bracket of if
			switch(keycode) {
			case KeyEvent.VK_LEFT:
				tryMove(curPiece, curX - 1, curY);
				break;
			case KeyEvent.VK_RIGHT:
				tryMove(curPiece, curX + 1, curY);
				break;
			case KeyEvent.VK_DOWN:
				tryMove(curPiece.rotateRight(), curX, curY);
				break;
			case KeyEvent.VK_UP:
				tryMove(curPiece.rotateLeft(), curX, curY);
				break;
			case KeyEvent.VK_SPACE:
				dropDown();
				break;
			case KeyEvent.VK_D:
				oneLineDown();
				break;
			}// Ending bracket of switch
		}// Ending bracket of method keyPressed
	}// Ending bracket of inner class TAdapter


}// Ending bracket of class Board