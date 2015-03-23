package com.majy.tictactoe.controller;

import com.majy.tictactoe.joue.Engine;
import com.majy.tictactoe.model.EtatDuJoue;
import com.majy.tictactoe.model.Joueur;
import com.majy.tictactoe.util.Camp;
import com.majy.tictactoe.util.JoueurType;

public class MultiPlayerController implements Controller {
	
	public MultiPlayerController(String nom1, String nom2, int profondeur, int n){
		Engine.getInstance().init(new Joueur(JoueurType.HUMAIN, nom1, Camp.X),
				new Joueur(JoueurType.HUMAIN, nom2, Camp.O),
				profondeur, n);	
	}
	
	@Override
	public EtatDuJoue buttonClick(int i, int j) {
		return Engine.getInstance().tour(i, j);
	}

}
