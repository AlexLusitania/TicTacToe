package com.majy.tictactoe.controller;

import com.majy.tictactoe.joue.Engine;
import com.majy.tictactoe.model.EtatDuJoue;
import com.majy.tictactoe.model.Joueur;
import com.majy.tictactoe.util.Camp;
import com.majy.tictactoe.util.JoueurType;

public class SinglePlayerController implements Controller {

	public SinglePlayerController(Joueur joueur, int profondeur, int n, String cpu_name){
		Joueur cpu = new Joueur(JoueurType.CPU, cpu_name);
		if(joueur.getCamp() == Camp.X){
			Engine.getInstance().init(joueur, cpu, profondeur, n);
		} else {
			Engine.getInstance().init(cpu, joueur, profondeur, n);
		}		
	}
	
	@Override
	public EtatDuJoue buttonClick(int i, int j) {
		return Engine.getInstance().tour(i, j);
	}

}
