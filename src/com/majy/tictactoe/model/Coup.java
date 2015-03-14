package com.majy.tictactoe.model;

public class Coup {
	int ligne;
	int col;
	
	public void setCoord(int ligne, int col) {
		this.ligne = ligne;
		this.col = col;
	}

	public int getLigne() {
		return ligne;
	}

	public int getCol() {
		return col;
	}	
}