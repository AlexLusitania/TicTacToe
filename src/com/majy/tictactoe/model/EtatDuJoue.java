package com.majy.tictactoe.model;

public class EtatDuJoue {
	private Grille grille;
	private Coup dernierCoup;
	private Joueur dernierJoueur;	
	private Joueur prochJoueur;
	
	public EtatDuJoue(Grille grille, Coup dernierCoup, Joueur dernierJoueur, Joueur prochJoueur) {
		this.grille = grille;
		this.dernierCoup = dernierCoup;
		this.dernierJoueur = dernierJoueur;
		this.prochJoueur = prochJoueur;
	}

	public Grille getGrille() {
		return grille;
	}
	
	public Coup getDernierCoup() {
		return dernierCoup;
	}

	public Joueur getDernierJoueur() {
		return dernierJoueur;
	}

	public Joueur getProchJoueur() {
		return prochJoueur;
	}
	
}
