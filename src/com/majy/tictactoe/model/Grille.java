package com.majy.tictactoe.model;

public class Grille {
	Camp[][] cases;

    public Grille(int rows, int cols){
        cases = new Camp[rows][cols];
        for(int i = 0; i < rows; ++i){
        	for(int j = 0; j < cols; ++j){
        		cases[i][j] = Camp.VIDE;
            }
        }
    }

    public Grille(Camp[][] cases){
        this.cases = cases;
    }

    public Grille copier(){
        return new Grille(cases.clone());
    }

    public Camp getCase(int row, int col){
        return cases[row][col];
    }

    public void setCase(int row, int col, Camp val){
        cases[row][col] = val;
    }
    
    public int getRows(){
    	return cases.length;
    }
    
    public int getCols(){
    	return cases[0].length;
    }

    public boolean joueurGagne(Camp camp){
        boolean gagne = false;

        //lignes
        for(int i = 0; i < cases.length; ++i){
            gagne = true;
            for(int j = 0; j < cases[i].length; ++j){
                if(cases[i][j] != camp){
                    gagne = false;
                    break;
                }
            }
        }
        if(gagne){return true;}

        //colonnes
        for(int i = 0; i < cases[0].length; ++i){
            gagne = true;
            for(int j = 0; j < cases.length; ++j){
                if(cases[j][i] != camp){
                    gagne = false;
                    break;
                }
            }
        }

        //diagonales
        gagne = true;
        for(int i = 0; i < cases.length; ++i){
            if(cases[i][i] != camp){
                gagne = false;
                break;
            }
        }
        if(gagne){return true;}
        gagne = true;
        for(int i = 0; i < cases.length; ++i){
            if(cases[i][cases.length-i-1] != camp){
                gagne = false;
                break;
            }
        }
        if(gagne){return true;}

        return false;
    }
    
    public int lignesContiennentPas(Camp camp){
    	int count = 0;
    	for(int i = 0; i < cases.length; ++i){
    		boolean contient = false;
            for(int j = 0; j < cases[i].length; ++j){
                if(cases[i][j] == camp){
                	contient = true;
                    break;
                }
            } 
            if(! contient){
        		count++;
    		}
        }
    	return count;
    }
    
    public int colonnesContiennentPas(Camp camp){
    	int count = 0;
    	for(int i = 0; i < cases[0].length; ++i){
    		boolean contient = false;
            for(int j = 0; j < cases.length; ++j){
                if(cases[j][i] == camp){
                	contient = true;
                    break;
                }
            } 
            if(! contient){
        		count++;
    		}
        }
    	return count;
    }
    
    public int diagContiennentPas(Camp camp){
    	int count = 0;
    	boolean contient = false;
    	for(int i = 0; i < cases.length; ++i){
    		if(cases[i][i] == camp){
            	contient = true;
                break;
            }            
        }
    	if(! contient){
    		count++;
		}
    	
    	contient = false;
    	for(int i = 0; i < cases.length; ++i){
    		if(cases[i][cases.length-i-1] == camp){
            	contient = true;
                break;
            }            
        }
    	if(! contient){
    		count++;
		}
    	
    	return count;
    }
    
    public int coupsGagnants(Camp camp){
    	int coups = 0;
    	for(int i = 0; i < cases.length; ++i){
    		for(int j = 0; j < cases[i].length; ++j){
        		if(cases[i][j] == Camp.VIDE){
        			cases[i][j] = camp;
        			if(joueurGagne(camp)){
        				coups++;
        			}      			
        			cases[i][j] = Camp.VIDE;
        		}
        	}
    	}
    	return coups;
    }
    
    public int estimerLignesDansLaGrilleCamp(Camp camp){
    	Camp campAdv = camp.adv();
    	return lignesContiennentPas(campAdv) +
    			colonnesContiennentPas(campAdv) +
    			diagContiennentPas(campAdv);   	
    }
    
    public int estimerLignesDansLaGrille(Camp camp){   	
    	return 2 * ( estimerLignesDansLaGrilleCamp(camp) - estimerLignesDansLaGrilleCamp(camp.adv()));   	
    }
    
    /**
     * Estimation de coups possibles pour gagner / perdre
     * priorite pour gagner (CPU peut prevenir default si jouer peut gagner mais cpu a coup ) 
     * */
    public int estimerCoupsDansGrilleAbsGagneDef(){    	
    	int coups = coupsGagnants(Camp.CPU);
    	if(coups > 0){
    		return 50 * coups;   	
    	}
    	
    	coups = coupsGagnants(Camp.JOUER);
    	if(coups > 0){
    		return -50 * coups;   	
    	}
    	return 0;
    }

    /**
     * Estimation de coups possibles pour gagner / perdre
     * priorite pour perdre (CPU peut pas prevenir default si jouer peut gagner et il a coup ) 
     * */	
    public int estimerCoupsDansGrilleAbsDefGagne(){ 	
    	int coups = coupsGagnants(Camp.JOUER);
    	if(coups > 0){
    		return -50 * coups;   	
    	}
    	
    	coups = coupsGagnants(Camp.CPU);
    	if(coups > 0){
    		return 50 * coups;   	
    	}
    	
    	return 0;
    }
    
    public int estimerCoupsDansGrilleAbs(Camp camp){ 	
    	if(joueurGagne(Camp.CPU)){return 100;}
    	if(joueurGagne(Camp.JOUER)){return -100;}
    	
    	return (camp == Camp.CPU) ? estimerCoupsDansGrilleAbsDefGagne() : 
    		estimerCoupsDansGrilleAbsGagneDef();
    }
    
    public int estimerGrille(Camp camp){
    	return estimerLignesDansLaGrille(camp) + estimerCoupsDansGrilleAbs(camp);
    }
    
    public boolean grilleFinie(Camp camp){
    	return (joueurGagne(camp.adv()) ||
    			coupsGagnants(camp.adv()) > 0 ||
    			joueurGagne(camp) ||
    			coupsGagnants(camp) > 0 );   	   	
    }
    
    private boolean grilleRemplie(){
    	for(int i = 0; i < cases.length; ++i){
        	for(int j = 0; j < cases[i].length; ++j){
        		if(cases[i][j] == Camp.VIDE){
        			return false;
        		}
            }
        }
    	return true;
    }
    
    public boolean partieFinie(){
    	return joueurGagne(Camp.CPU) || joueurGagne(Camp.JOUER) || grilleRemplie();
    }
    
    public void affiche(){
    	for(int i = 0; i < getRows(); ++i){
    		System.out.print(" "+cases[i][0]);
    		for(int j = 1; j < getCols(); ++j){
            	System.out.print(" | "+cases[i][j]);
            }
            System.out.println();
    	}
    }
}