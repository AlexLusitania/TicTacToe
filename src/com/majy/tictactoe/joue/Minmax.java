package com.majy.tictactoe.joue;

import java.util.LinkedList;
import java.util.List;

import com.majy.tictactoe.model.Coup;
import com.majy.tictactoe.model.Grille;
import com.majy.tictactoe.util.Camp;

public class Minmax {
	
	public static Coup trouverCoup(Grille grille, int profondeur, Camp camp){
		if(profondeur == 0){
    		if(Math.random() < 0.5){
    			List<Coup> coupsDisponiles = new LinkedList<Coup>();
    			for(int i = 0; i < grille.getRows(); ++i){
    	            for(int j = 0; j < grille.getCols(); ++j){
    	            	if(grille.getCase(i,j) == Camp.VIDE){
    	            		coupsDisponiles.add(new Coup(i,j));
    	            	}
    	            }
    	        }
    			return  coupsDisponiles.get((int)Math.round(Math.random()*coupsDisponiles.size()));
    		} else {
    			profondeur = 1;
    		}
    	}
    	
        int casesVides = 0;
        for(int i = 0; i < grille.getRows(); ++i){
            for(int j = 0; j < grille.getCols(); ++j){
                if(grille.getCase(i,j) == Camp.VIDE){
                	casesVides++;
                }
            }
        }
        if(casesVides > 15){
        	profondeur = Math.min(profondeur, 1);
        } else if(casesVides > 10){
        	profondeur = Math.min(profondeur, 2);
        }
        
        Coup coup = new Coup();
        minmax(grille, profondeur, camp, coup, true);
        
        return coup;
	}
	
    public static int minmax(Grille grille, int profondeur, Camp camp, Coup coup, boolean max){
    	
        int valeur = (max) ? Integer.MIN_VALUE : Integer.MAX_VALUE;      
        
        for(int i = 0; i < grille.getRows(); ++i){
            for(int j = 0; j < grille.getCols(); ++j){
                if(grille.getCase(i,j) == Camp.VIDE){
                    grille.setCase(i, j, camp);

                    int valeurNouv;
                    if(profondeur == 1 || grille.grilleFinie(camp) || grille.grilleRemplie()){
                        valeurNouv = grille.estimerGrille(camp, max);
                    } else {
                        valeurNouv = Minmax.minmax(grille, profondeur-1, camp.adv(), coup, !max);
                    }

                    if(max && valeurNouv > valeur){
                        valeur = valeurNouv;
                        coup.setCoord(i,j);
                    } else if(!max && valeurNouv < valeur){
                        valeur = valeurNouv;
                        coup.setCoord(i,j);
                    }
                    
                    grille.setCase(i, j, Camp.VIDE);
                }
            }
        }

        return valeur;
    }

}
