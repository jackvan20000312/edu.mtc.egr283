package edu.mtc.egr283;

import java.util.Random;

public class Shape {
	private Random randomizer;
	private int[][] coords;
	private int[][][] coordsTable;
	private Tetrominoes pieceShape;

	public Shape() {
		this.randomizer = new Random();
		this.coords = new int[4][2];
		this.coordsTable = new int[][][] {
			{ { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 } },
			{ { 0, -1 }, { 0, 0 }, { -1, 0 }, { -1, 1 } },
			{ { 0, -1 }, { 0, 0 }, { 1, 0 }, { 1, 1 } },
			{ { 0, -1 }, { 0, 0 }, { 0, 1 }, { 0, 2 } },
			{ { -1, 0 }, { 0, 0 }, { 1, 0 }, { 0, 1 } },
			{ { 0, 0 }, { 1, 0 }, { 0, 1 }, { 1, 1 } },
			{ { -1, -1 }, { 0, -1 }, { 0, 0 }, { 0, 1 } },
			{ { 1, -1 }, { 0, -1 }, { 0, 0 }, { 0, 1 } },
		};
		this.setShape(Tetrominoes.NoShape);
	}// Ending bracket of constructor ‘

	public void setShape(Tetrominoes shape) {
		for(int i = 0; i < coords.length; ++i) {
			for(int j = 0; j < coords[i].length; ++j) {
				this.coords[i][j] = this.coordsTable[shape.ordinal()][i][j];
			}// Ending bracket of INNER for loop
		}// Ending bracket of OUTER for loop
		this.pieceShape = shape;
	}// Ending bracket of method setShape

	public Tetrominoes getShape() {
		return this.pieceShape;
	}// Ending bracket of method getShape

	private void setX(int index, int x) {
		this.coords[index][0] = x;
	}// Ending bracket of method setX

	private void setY(int index, int y) {
		this.coords[index][1] = y;
	}// Ending bracket of method setY

	public int x(int index) {
		return this.coords[index][0];
	}// Ending bracket of method x

	public int y(int index) {
		return this.coords[index][1];
	}// Ending bracket of method y

	public void setRandomShape() {
		Tetrominoes[] shapes = Tetrominoes.values();
		this.setShape(shapes[this.randomizer.nextInt(7) + 1]);
	}// Ending bracket of method setRandomShape

	public int minX() {
		int m = this.coords[0][0];
		for(int i = 1; i < this.coords.length; ++i) {
			m = Math.min(m, this.coords[i][0]);
		}// Ending bracket of for loop
		return m;
	}// Ending bracket of method minX

	public int minY() {
		int m = this.coords[0][1];
		for(int i = 1; i < this.coords.length; ++i) {
			m = Math.min(m, this.coords[i][1]);
		}// Ending bracket of for loop
		return m;
	}// Ending bracket of method minX

	public Shape rotateLeft() {
		Shape result = new Shape();
		if(this.pieceShape == Tetrominoes.SquareShape) {
			result = this;
		} else {
			result.pieceShape = this.pieceShape;
			for(int i = 0; i < this.coords.length; ++i) {
				result.setX(i, this.y(i));
				result.setY(i, -this.x(i));
			}// Ending bracket of for loop
		}// Ending bracket of if
		return result;
	}// Ending bracket of method rotateLeft
	
	public Shape rotateRight() {
		Shape result = new Shape();
		if(this.pieceShape == Tetrominoes.SquareShape) {
			result = this;
		} else {
			result.pieceShape = this.pieceShape;
			for(int i = 0; i < this.coords.length; ++i) {
				result.setX(i, -this.y(i));
				result.setY(i, this.x(i));
			}// Ending bracket of for loop
		}// Ending bracket of if
		return result;
	}// Ending bracket of method rotateRight


}// Ending bracket of class Shape
