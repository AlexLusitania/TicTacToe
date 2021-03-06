package com.majy.tictactoe.vue;

import java.util.concurrent.atomic.AtomicBoolean;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
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
    private EtatDuJoue etat_joue;
    private TextView textView1;
    private TextView textView2;
    // Sound
    private SoundPool soundPool_fanf;
	private int soundID_fanf;
	boolean plays_fanf = false, loaded_fanf = false;
	private SoundPool soundPool_go;
	private int soundID_go;
	boolean plays_go = false, loaded_go = false;
	private SoundPool soundPool_mj;
	private int soundID_mj;
	boolean plays_mj = false, loaded_mj = false;
	
	float actVolume, maxVolume, volume;
	AudioManager audioManager;
	int counter;
	
	private AtomicBoolean enAttente = new AtomicBoolean(false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        n = Integer.parseInt(pref.getString("pref_size", "3"));
        
        Bundle b = getIntent().getExtras();
        int mode_launched = b.getInt("mode");

        getAdaptedController(mode_launched);

        //// Audio
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		actVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		volume = actVolume;
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		counter = 0;
		
		soundPool_fanf = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		soundPool_fanf.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
				loaded_fanf = true;
			}
		});
		soundID_fanf = soundPool_fanf.load(this, R.raw.fanfare, 1);
		//
		soundPool_go = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		soundPool_go.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
				loaded_go = true;
			}
		});
		soundID_go = soundPool_go.load(this, R.raw.gameover, 1);
		//
		soundPool_mj = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		soundPool_mj.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
				loaded_mj = true;
			}
		});
		soundID_mj = soundPool_mj.load(this, R.raw.minijump, 1);
        
        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        
        setContentView(R.layout.game_layout);
        grid_layout = (LinearLayout) findViewById(R.id.grid_layout);
        table = new TableLayout(this);
        
        grid_layout.addView(table);

        createButtons();

        updateInfos(mode_launched);
        
        EtatDuJoue joue = Engine.getInstance().getEtatDuJoue();
        if(joue != null){
        	afficher(joue);
        } else { 
        	initJoue(mode_launched, n);
        }

    }

    private void updateInfos(int mode_launched){
    	pref = PreferenceManager.getDefaultSharedPreferences(this);
        String pref_username = pref.getString("pref_username", getString(R.string.default_username));
        
        TextView info1 = (TextView) findViewById(R.id.textView1);
        TextView info2 = (TextView) findViewById(R.id.textView2);
        
        info1.setText(pref_username);
        if(mode_launched == 1){
        	info2.setText(R.string.cpu_name);
        }
        else{
        	info2.setText(R.string.default_username_2);
        }
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
        for (int i=0; i<n; i++){
            for (int j=0; j<n; j++){
                (btns[i][j]).setBackgroundResource(R.drawable.blank);
            }
        }
    } 
    
    private void turn(int i, int j){
    	this.etat_joue = controller.buttonClick(i, j);
    	if(this.etat_joue == null){
    		enAttente.set(false);
    		return;
    	}
		afficher(this.etat_joue);
		
		if(this.etat_joue.getGrille().partieFinie()){
			afficherResultats(this.etat_joue);
		} else {
			checkCPUTurn(this.etat_joue);
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
    	textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
    	this.runOnUiThread(new Runnable(){
			@Override
			public void run() {
				if(etat.getProchJoueur().getCamp() == Camp.X){
					// c'est à X de jouer
					textView1.setBackgroundColor(Color.parseColor("#0099CC"));
					textView1.setTypeface(null, Typeface.BOLD);
					
					textView2.setBackgroundColor(Color.TRANSPARENT);
					textView2.setTypeface(null, Typeface.NORMAL);
				} else {
					// c'est à O de jouer
					textView2.setBackgroundColor(Color.parseColor("#0099CC"));
					textView2.setTypeface(null, Typeface.BOLD);
					
					textView1.setBackgroundColor(Color.TRANSPARENT);
					textView1.setTypeface(null, Typeface.NORMAL);
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
					if(joue.getDernierJoueur().getCamp() == Camp.X){
						playSoundFanfare();
					} else {
						playSoundGO();
					}
			    } else {
			    	text = getString(R.string.draw);
			    	playSoundGO();
			    }
				
				Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
				
				
				Intent intent = new Intent(GameActivity.this, MainActivity.class);
				
				enAttente.set(false);
				Engine.getInstance().effacerJoue();
				startActivity(intent);
			}
    	});  	
    }

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
	}
	
	public void playSoundFanfare() {
		// Is the sound loaded does it already play?
		if (loaded_fanf && !plays_fanf) {
			soundPool_fanf.play(soundID_fanf, volume, volume, 1, 0, 1f);
			counter = counter++;
			plays_fanf = true;
		}
	}
	
	public void playSoundGO() {
		// Is the sound loaded does it already play?
		if (loaded_go && !plays_go) {
			soundPool_go.play(soundID_go, volume, volume, 1, 0, 1f);
			counter = counter++;
			Toast.makeText(this, "Played sound", Toast.LENGTH_SHORT).show();
			plays_go = true;
		}
	}
	
	public void playSoundMiniJump() {
		// Is the sound loaded does it already play?
		if (loaded_mj && !plays_mj) {
			soundPool_mj.play(soundID_mj, volume, volume, 1, 0, 1f);
			counter = counter++;
			Toast.makeText(this, "Played sound", Toast.LENGTH_SHORT).show();
			plays_mj = true;
		}
	}
	
}