package com.example.elliott.alliancepicker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.content.pm.ActivityInfo;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class Tutorial extends AppCompatActivity {

    ColorScheme selectedColor = new ColorScheme("ReadOnly", "", "", "", "", "").selectedColor;

    Team teamClass = new Team("ReadOnly", "0000", new int[0]);
    int rowNum = 0;
    int slide = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getRequestedOrientation();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial);

        displayTeams();
        ((InstantizedVars) this.getApplication()).firstLoad = false;


        Results_CreateRow("1. 1234 (Basic Team) = 1000", true);
        moveON();

    }
    private void makeSlides(){
        TextView descrip = (TextView) findViewById(R.id.description);
        switch (slide) {
            case 1:
                System.out.println("In Switch 1");
                Results_CreateRow("1. 1234 (Basic Team) = 1000", true);
                break;
            case 2:
                System.out.println("In Switch 2");
                descrip.setText("This is the settings menu, where you can look at and change a" +
                        " team's name, or your previous score for a team");
                Results_CreateRow("1. 1234 (Basic Team) = 1000", true);
                break;
            case 3:
                System.out.println("In Switch 3");
                descrip.setText("This is the more info button, where you can compare teams stats to the average," +
                    " and see it's strengths and weaknesses.");
                Results_CreateRow("1. 1234 (Basic Team) = 1000", true);
                break;
            case 4:
                System.out.println("In Switch 4");
                descrip.setText("This is the delete button, which will clear the team from the app.");
                Results_CreateRow("1. 1234 (Basic Team) = 1000", true);
                break;
            case 5:
                System.out.println("In Switch 5, returning to Main");
                Intent myIntent = new Intent(this, MainActivity.class);
                myIntent.putExtra("loadType", "-1"); //Optional parameters
                myIntent.putExtra("runType", "0");
                this.startActivity(myIntent);
                break;
        }
    }
    private void moveON(){
        Button ok = (Button)findViewById(R.id.gotIt);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slide++;
                makeSlides();
            }
        });
        Button forgot = (Button)findViewById(R.id.IForgot);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(slide > 1){
                    slide--;
                }
                makeSlides();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void Results_CreateRow(String dispText, Boolean buttons) {
        TableLayout tl = (TableLayout) findViewById(R.id.TutorialTable);

        ScrollView sv = (ScrollView) findViewById(R.id.top_results);
        sv.setBackgroundColor(Color.parseColor(selectedColor.EdgeBgColor));
        tl.removeAllViews();
        TableRow tr1 = new TableRow(this);
        tr1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        TextView textview = (TextView) getLayoutInflater().inflate(R.layout.results_row, null);
        /* Team Text */
        textview.setId(5300 + rowNum);
        textview.setTextColor(Color.parseColor(selectedColor.TxtColor));
        textview.setText(dispText);

        if (rowNum % 2 == 0) {
            tr1.setBackgroundColor(Color.parseColor(selectedColor.BgColor));
        } else {
            tr1.setBackgroundColor(Color.parseColor(selectedColor.BgColor2));
        }

        tr1.addView(textview);

        if (buttons) {
            ImageView create;
            ImageView score;
            ImageView delete;
            if (slide == 2) {
                create = (ImageView) getLayoutInflater().inflate(R.layout.tutorial_settings, null);
            } else {
                create = (ImageView) getLayoutInflater().inflate(R.layout.results_settings, null);
            }
            create.setId(5000 + rowNum);

            if (slide == 3) {
                score = (ImageView) getLayoutInflater().inflate(R.layout.tutorial_score, null);
            } else {
                score = (ImageView) getLayoutInflater().inflate(R.layout.results_score, null);
            }
            score.setId(5100 + rowNum);


        /* Delete Specific Team */
            if (slide == 4) {
                delete = (ImageView) getLayoutInflater().inflate(R.layout.tutorial_delete_specific, null);
            } else {
                delete = (ImageView) getLayoutInflater().inflate(R.layout.results_delete, null);
            }
            delete.setId(5200 + rowNum);

            tr1.addView(create);
            tr1.addView(score);
            tr1.addView(delete);
        }

        tl.addView(tr1, new TableLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        rowNum++;
    }

    public void displayTeams() {
        rowNum = 0;
        teamClass.refresh();
        if (teamClass.allTeams.toArray().length == 0) {
            Results_CreateRow("Press the + to create a new team!", false);
        } else {
            for (int cntr = 0; cntr < teamClass.allTeams.toArray().length; cntr++) {
                Results_CreateRow((cntr + 1) + ". " + teamClass.allTeams.get(cntr).Number + "(" + teamClass.allTeams.get(cntr).Name + ") = " + teamClass.allTeams.get(cntr).totalScore(), true);
            }
        }
    }

    public void goToSettings(MenuItem item) {
        Intent myIntent = new Intent(this, CustomizeActivity.class);
        this.startActivity(myIntent);
    }
}