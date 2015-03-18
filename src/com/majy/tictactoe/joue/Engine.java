package com.majy.tictactoe.joue;

import com.majy.tictactoe.model.Coup;
import com.majy.tictactoe.model.EtatDuJoue;
import com.majy.tictactoe.model.Grille;
import com.majy.tictactoe.model.Joueur;
import com.majy.tictactoe.util.Camp;
import com.majy.tictactoe.util.JoueurType;

public class Engine {
	
	private static Engine instance = new Engine();
	
	private int profondeur;
	private Joueur joueurX;
	private Joueur joueurO;
	private Joueur joueur;
	private Grille grille;	
	
	public static Engine getInstance() {
		return instance;
	}

	public void init(Joueur joueurX, Joueur joueurO, int profondeur, int tailleDeGrille){
		this.joueurX = joueurX;
		this.joueurO = joueurO;
		this.profondeur = profondeur;
		this.grille = new Grille(tailleDeGrille);
		
		this.joueur = this.joueurX;
		this.joueurX.setCamp(Camp.X);
		this.joueurO.setCamp(Camp.O);
	}

	
	public EtatDuJoue tour(int i, int j){
				
    	Coup coup;   	
    	if(joueur.getType() == JoueurType.CPU){
    		coup = Minmax.trouverCoup(grille, profondeur, joueur.getCamp());
    	} else {
    		if(grille.getCase(i, j) != Camp.VIDE){
    			return null;
    		}
    		
    		coup = new Coup(i, j);
    	}   	
    	grille.setCase(coup.getLigne(), coup.getCol(), joueur.getCamp());  	
    	
    	Joueur dernierJouer = joueur;
    	joueur = (joueur == joueurX) ? joueurO : joueurX;
    	
    	return new EtatDuJoue(grille, coup, dernierJouer, joueur);
    }
	
}
