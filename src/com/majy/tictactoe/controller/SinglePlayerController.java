package com.majy.tictactoe.controller;

import com.majy.tictactoe.joue.Engine;
import com.majy.tictactoe.model.EtatDuJoue;

public class SinglePlayerController implements Controller {
	
	@Override
	public EtatDuJoue buttonClick(int i, int j) {
		return Engine.getInstance().tour(i, j);
	}

}