package com.example.tictactoeui;

//import android.support.v7.app.ActionBarActivity;
//import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;


public class GameActivity extends Activity {

    private LinearLayout mainLayout;
    private TableLayout table;
    private int N = 3;
    private ImageButton[][] btns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);

        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);

        table = new TableLayout(this);
        mainLayout.addView(table);

        createButtons();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

        btns = new ImageButton[N][N];
        for (int i=0; i<N; i++)
        {
            TableRow row = new TableRow(this);
            TableRow.LayoutParams paramsBtn = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1);
            for (int j=0; j<N; j++)
            {
                ImageButton btn = new ImageButton(this);
               // btn.setBackgroundResource(R.drawable.posvide);
                btns[i][j] = btn;
                btn.setPadding(0,0,0,0);
                btn.setId(i*N + j);
                btn.setLayoutParams(paramsBtn);
                row.addView(btn);
                btn.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View view) {
                        int id = view.getId();
                        int i = id/N;
                        int j = id - N*i;
                        //game.controller().buttonClick(i, j, game);
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

        for (int i=0; i<N; i++){
            for (int j=0; j<N; j++){
                (btns[i][j]).setBackgroundResource(R.drawable.blank);

            }

        }

    }


}