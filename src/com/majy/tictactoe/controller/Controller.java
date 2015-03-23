package com.majy.tictactoe.controller;

import com.majy.tictactoe.model.EtatDuJoue;

public interface Controller {
	public EtatDuJoue buttonClick(int i, int j);
}