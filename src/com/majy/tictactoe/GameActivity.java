package com.majy.tictactoe;

import android.app.Activity;
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
import android.widget.Toast;

@SuppressWarnings("unused")
public class GameActivity extends Activity {

    private LinearLayout mainLayout;
    private TableLayout table;
    private int n;
    private ImageButton[][] btns;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Bundle b = getIntent().getExtras();
        int mode_launched = b.getInt("mode");
        Toast.makeText(getApplicationContext(), "mode launched: " + mode_launched, Toast.LENGTH_SHORT).show();
        
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        n = Integer.parseInt(pref.getString("pref_size", "3"));
        
        setContentView(R.layout.game_layout);
        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        table = new TableLayout(this);
        
        mainLayout.addView(table);

        createButtons();
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
                // btn.setBackgroundResource(R.drawable.posvide);
                btns[i][j] = btn;
                btn.setPadding(0,0,0,0);
                btn.setId(i*n + j);
                btn.setLayoutParams(paramsBtn);
                row.addView(btn);
                /*btn.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View view){
                        int id = view.getId();
                        int i = id/n;
						int j = id - n*i;
                        //game.controller().buttonClick(i, j, game);
                    }
                });*/

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

}