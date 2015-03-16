package com.majy.tictactoe.vue;

import java.util.concurrent.atomic.AtomicBoolean;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
        Toast.makeText(getApplicationContext(), "mode launched: " + mode_launched, Toast.LENGTH_SHORT).show();
        getAdaptedController(mode_launched, n);
        
        setContentView(R.layout.game_layout);
        grid_layout = (LinearLayout) findViewById(R.id.grid_layout);
        table = new TableLayout(this);
        
        grid_layout.addView(table);

        createButtons();
        updateInfos();     
    }

    private void updateInfos(){
    	pref = PreferenceManager.getDefaultSharedPreferences(this);
        String pref_username = pref.getString("pref_username", getString(R.string.default_username));
        
        TextView info1 = (TextView) findViewById(R.id.textView1);
        TextView info2 = (TextView) findViewById(R.id.textView2);
        
        info1.setText(pref_username);
        info2.setText(R.string.cpu_name);
    }

    private void getAdaptedController(int mode_launched, int n) {
    	//Difficulte:
		//6 - difficile
		//2 - medium
		//0 - facile (demi-random)
		int profondeur = 6;
		
    	switch(mode_launched){
			case 2:{
				controller = new MultiPlayerController("Player1","Player2", profondeur, n);
				break;
			}
			case 3:{
				controller = new BluetoothController();
				break;
			}
			default:{				
				//Jouer avec 'X'
				controller = new SinglePlayerController(new Joueur(JoueurType.HUMAIN,"Max", Camp.X), profondeur, n);				
				
				//Jouer avec 'O'
				//controller = new SinglePlayerController(new Joueur(JoueurType.HUMAIN,"Max", Camp.O), profondeur, n);
				//afficher(controller.buttonClick(0, 0));
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
						
						turn(i,j);
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
    	if(enAttente.compareAndSet(false, true)){
    		boolean finie = false;
	    	EtatDuJoue joue = controller.buttonClick(i, j);
			afficher(joue);
			finie = joue.getGrille().partieFinie();
					
			while(! finie && joue.getProchJoueur().getType() == JoueurType.CPU){				
				
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				joue = controller.buttonClick(0, 0);
				afficher(joue);
				finie = joue.getGrille().partieFinie();
			}
			
			enAttente.set(false);
			
			if(finie){
	    		afficherResultats(joue);
	    	}
    	}
    	
    }
    
    private void afficher(EtatDuJoue etat){
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
    
    private void afficherResultats(EtatDuJoue joue){
    	String text = null;
		if(joue.getGrille().joueurGagne(joue.getDernierJoueur().getCamp())){
			text = joue.getDernierJoueur().getName() + " a gagné !";
	    } else {
	    	text = "Egalité !";
	    }
		
		enAttente.set(false);
		Toast.makeText(getApplicationContext(), text , Toast.LENGTH_LONG).show();
		
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
    }

}