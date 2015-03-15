package com.majy.tictactoe.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Minmax {
	private static int profondeur = 5;
	
	public static void setProfondeur(int valeur){
		profondeur = valeur;
	}
	
    public static int estimer(Grille grille, int profondeur, Camp camp, Coup coup){
        int valeur = (camp == Camp.JOUER) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        
        for(int i = 0; i < grille.getRows(); ++i){
            for(int j = 0; j < grille.getCols(); ++j){
                if(grille.getCase(i,j) == Camp.VIDE){
                    grille.setCase(i, j, camp);

                    int valeurNouv;
                    if(profondeur == 1 || grille.grilleFinie(camp)){
                        valeurNouv = grille.estimerGrille(camp);
                    } else {
                        valeurNouv = Minmax.estimer(grille, profondeur-1,camp.adv(),coup);
                    }

                    if(camp == Camp.JOUER && valeurNouv > valeur){
                        valeur = valeurNouv;
                        coup.setCoord(i,j);
                    } else if(camp == Camp.CPU && valeurNouv < valeur){
                        valeur = valeurNouv;
                        coup.setCoord(i,j);
                    }
                    
                    grille.setCase(i, j, Camp.VIDE);
                }
            }
        }

        return valeur;
    }
    
    public static void tour(Grille grille, Camp camp){
    	Coup coup = new Coup();
    	
    	if(camp == Camp.CPU){
    		estimer(grille, profondeur, camp, coup);
    	} else {
    		grille.affiche();
    		
    		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    		
    		int ligne = -1;
    		do{  			
    			System.out.print("Ligne -> ");
    			try{
    				ligne = Integer.valueOf(in.readLine()) - 1;	
    			} catch (IOException e) {	}
    		} while (ligne < 0 || ligne >= grille.getRows());
    		
    		int col = -1;
    		do{  			
    			System.out.print("Col -> ");
    			try{
    				col = Integer.valueOf(in.readLine()) - 1;	
    			} catch (IOException e) {	}
    		} while (col < 0 || col >= grille.getCols());
    		
    		coup.setCoord(ligne, col);
    	}
    	
    	grille.setCase(coup.getLigne(), coup.getCol(), camp);
    }
    
    public static void afficherResultat(Grille grille){
    	if(grille.joueurGagne(Camp.JOUER)){
    		System.out.println("Jouer a gagné");
    	} else if(grille.joueurGagne(Camp.CPU)){
    		System.out.println("CPU a gagné");
    	} else {
    		System.out.println("Draw");
    	}
    }
    
    public static void main(String[] args){
    	Grille grille = new Grille(3,3);
    	Camp tour = Camp.CPU;
    	
    	while(! grille.partieFinie() ){
    		tour(grille, tour);
    		tour = tour.adv();   		
    	}
    	
    	grille.affiche();
    	afficherResultat(grille);	
    }
}
