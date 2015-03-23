package com.majy.tictactoe.vue;

import java.util.concurrent.atomic.AtomicBoolean;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.majy.tictactoe.R;
import com.majy.tictactoe.controller.BluetoothController;
import com.majy.tictactoe.controller.Controller;
import com.majy.tictactoe.controller.MultiPlayerController;
import com.majy.tictactoe.controller.SinglePlayerController;
import com.majy.tictactoe.joue.Engine;
import com.majy.tictactoe.model.EtatDuJoue;
import com.majy.tictactoe.model.Joueur;
import com.majy.tictactoe.util.Camp;
import com.majy.tictactoe.util.JoueurType;

public class GameActivity extends Activity {

	private LinearLayout grid_layout;
    private TableLayout table;
    private int n;
    private ImageButton[][] btns;
    private SharedPreferences pref;
    private Controller controller;

	private AtomicBoolean enAttente = new AtomicBoolean(false);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        n = Integer.parseInt(pref.getString("pref_size", "3"));
        
        Bundle b = getIntent().getExtras();
        int mode_launched = b.getInt("mode");
        //Toast.makeText(getApplicationContext(), "mode launched: " + mode_launched, Toast.LENGTH_SHORT).show();
        getAdaptedController(mode_launched);
        
        setContentView(R.layout.game_layout);
        grid_layout = (LinearLayout) findViewById(R.id.grid_layout);
        table = new TableLayout(this);
        
        grid_layout.addView(table);

        createButtons();
        updateInfos();
        
        EtatDuJoue joue = Engine.getInstance().getEtatDuJoue();
        if(joue != null){
        	afficher(joue);
        } else { 
        	initJoue(mode_launched, n);
        }
    }

    private void updateInfos(){
    	pref = PreferenceManager.getDefaultSharedPreferences(this);
        String pref_username = pref.getString("pref_username", getString(R.string.default_username));
        
        TextView info1 = (TextView) findViewById(R.id.textView1);
        TextView info2 = (TextView) findViewById(R.id.textView2);
        
        info1.setText(pref_username);
        info2.setText(R.string.cpu_name);
    }

    private void getAdaptedController(int mode_launched) {
    			
    	switch(mode_launched){
			case 2:{
				controller = new MultiPlayerController();
				break;
			}
			case 3:{
				controller = new BluetoothController();
				break;
			}
			default:{
				controller = new SinglePlayerController();
			}
		}
	}
    
    private void initJoue(int mode_launched, int n){
    	pref = PreferenceManager.getDefaultSharedPreferences(this);
        String pref_username = pref.getString("pref_username", getString(R.string.default_username));   	
        int profondeur = Integer.parseInt(pref.getString("pref_difficulty", "0"));
        
        switch(mode_launched){
			case 2: case 3:{
				Engine.getInstance().init(new Joueur(JoueurType.HUMAIN, pref_username, Camp.X),
						new Joueur(JoueurType.HUMAIN, getString(R.string.default_username_2), Camp.O),
						profondeur, n);	
				break;
			}
			/*
			case 3:{
				//TODO
				break;
			}
			*/
			default:{
				Camp joueurCamp = Camp.X;//Jouer avec 'X'
				Joueur joueur = new Joueur(JoueurType.HUMAIN, pref_username, joueurCamp);				
				Joueur cpu = new Joueur(JoueurType.CPU, getString(R.string.cpu_name));
				if(joueur.getCamp() == Camp.X){
					Engine.getInstance().init(joueur, cpu, profondeur, n);
				} else {
					Engine.getInstance().init(cpu, joueur, profondeur, n);
				}	
			}
		} 	    	
    	
    }


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    
    private void createButtons()
    {
        table.removeAllViews();
        table.setStretchAllColumns(true);

        btns = new ImageButton[n][n];
        for(int i=0; i<n; i++){
            TableRow row = new TableRow(this);
            TableRow.LayoutParams paramsBtn = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1);
            
            for (int j=0; j<n; j++){
                ImageButton btn = new ImageButton(this);
                btns[i][j] = btn;
                btn.setPadding(0,0,0,0);
                btn.setId(i*n + j);
                btn.setLayoutParams(paramsBtn);
                row.addView(btn);
                btn.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View view){
                        int id = view.getId();
                        int i = id/n;
						int j = id - n*i;
						
						//Prévenir les clicks quand le joueur adverse joue
						if(enAttente.compareAndSet(false, true)){
							turn(i,j);							
						}
                    }
                });

            }
            table.addView(row);
            table.setColumnShrinkable(i, true);
        }
        setButtons(); //initialiser les images
    }

    //initialiser les images avec l'image vide
    public void setButtons(){
        //Case c;
        for (int i=0; i<n; i++){
            for (int j=0; j<n; j++){
                (btns[i][j]).setBackgroundResource(R.drawable.blank);
            }
        }
    } 
    
    private void turn(int i, int j){
    	    	
    	EtatDuJoue joue = controller.buttonClick(i, j);
    	if(joue == null){
    		enAttente.set(false);
    		return;
    	}
		afficher(joue);
		
		if(joue.getGrille().partieFinie()){
			afficherResultats(joue);
		} else {
			checkCPUTurn(joue);
		}
    }
    
    private synchronized void checkCPUTurn(EtatDuJoue joue){
    	if(joue.getProchJoueur().getType() == JoueurType.CPU){
			new Thread(){
				@Override
				public void run(){
					SystemClock.sleep(500);
					turnCPU();
				}
			}.start();
		} else {
			enAttente.set(false);
		}
    }
    
    private synchronized void turnCPU(){
 		
    	EtatDuJoue joue = controller.buttonClick(0, 0);
		afficher(joue);
		
		if(joue.getGrille().partieFinie()){
			afficherResultats(joue);
		} else {
			checkCPUTurn(joue);
		}
    }
    
    private void afficher(final EtatDuJoue etat){
    	this.runOnUiThread(new Runnable(){
			@Override
			public void run() {
				if(etat.getProchJoueur().getCamp() == Camp.X){
					//TODO
					//montrer que c'est X qui fait le tour
				} else {
					//TODO
					//montrer que c'est O qui fait le tour
				}
				
				
				for (int i=0; i<n; i++){
		            for (int j=0; j<n; j++){
		            	switch(etat.getGrille().getCase(i, j)){
		            		case X: {
		            			(btns[i][j]).setBackgroundResource(R.drawable.croix1);
		            			break;
		            		}
		            		case O: {
		            			(btns[i][j]).setBackgroundResource(R.drawable.rond1);
		            			break;
		            		}
		            		case VIDE: {
		            			(btns[i][j]).setBackgroundResource(R.drawable.blank);
		            			break;
		            		}
		            	}
		            	(btns[i][j]).clearAnimation();
		            	(btns[i][j]).invalidate();
		            }
		        }
			}  		
    	});   	
    }
    
    private void afficherResultats(final EtatDuJoue joue){
    	this.runOnUiThread(new Runnable(){
			@Override
			public void run() {			
				String text = null;
				if(joue.getGrille().joueurGagne(joue.getDernierJoueur().getCamp())){
					text = joue.getDernierJoueur().getName() + " " + getString(R.string.player_win);
			    } else {
			    	text = getString(R.string.draw);
			    }
				
				Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
				
				
				Intent intent = new Intent(GameActivity.this, MainActivity.class);
				
				enAttente.set(false);
				Engine.getInstance().effacerJoue();
				startActivity(intent);
			}
    	});  	
    }
}