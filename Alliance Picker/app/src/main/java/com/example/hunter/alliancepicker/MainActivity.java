package com.example.hunter.alliancepicker;

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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.content.pm.ActivityInfo;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    ColorScheme selectedColor = ColorScheme.selectedColor;

    int rowNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getRequestedOrientation();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        if(InstantizedVars.firstLoad){
            getSave();
        }
        else{
            updateSave();
        }
        displayTeams();
        ((InstantizedVars) this.getApplication()).firstLoad = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        switch (id) {
            case R.id.delete_all:
                clearAllItems();
                break;
            case R.id.addTeam:
                Intent myIntent= new Intent(this, QuestionActivity.class);

                myIntent.putExtra("loadType", "-1"); //Optional parameters
                myIntent.putExtra("runType", "0");
                this.startActivity(myIntent);
                break;
            case R.id.menu_settings:
                Intent settingsIntent = new Intent(this, QuestionActivity.class);

                settingsIntent.putExtra("loadType", "-1"); //Optional parameters
                settingsIntent.putExtra("runType", "1");
                this.startActivity(settingsIntent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void Results_CreateRow(String dispText, Boolean buttons) {
        TableLayout tl = (TableLayout) findViewById(R.id.maintainable);

        ScrollView sv = (ScrollView) findViewById(R.id.top_results);
        sv.setBackgroundColor(Color.parseColor(selectedColor.EdgeBgColor));

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
            ImageView create = (ImageView) getLayoutInflater().inflate(R.layout.results_settings, null);
            create.setId(5000 + rowNum);
            create.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(MainActivity.this, QuestionActivity.class);
                    myIntent.putExtra("loadType", Integer.toString((v.getId() - 5000))); //Optional parameters
                    myIntent.putExtra("runType", "0");
                    MainActivity.this.startActivity(myIntent);
                }
            });

            ImageView score = (ImageView) getLayoutInflater().inflate(R.layout.results_score, null);
            score.setId(5100 + rowNum);
            score.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(MainActivity.this, InfoActivity.class);
                    myIntent.putExtra("loadType", Integer.toString(v.getId() - 5100)); //Optional parameters
                    MainActivity.this.startActivity(myIntent);
                }
            });


        /* Delete Specific Team */
            ImageView delete = (ImageView) getLayoutInflater().inflate(R.layout.results_delete, null);
            delete.setId(5200 + rowNum);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearTeam(v.getId() - 5200);
                    updateSave();
                }
            });

            tr1.addView(create);
            tr1.addView(score);
            tr1.addView(delete);
        }

        tl.addView(tr1, new TableLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        rowNum++;
    }

    public void displayTeams() {
        rowNum = 0;
        Team.refresh();
        if (Team.allTeams.toArray().length == 0) {
            Results_CreateRow("Press the + to create a new team!", false);
        } else {
            for (int cntr = 0; cntr < Team.allTeams.toArray().length; cntr++) {
                Results_CreateRow((cntr + 1) + ". " + Team.allTeams.get(cntr).Number + "(" + Team.allTeams.get(cntr).Name + ") = " + Team.allTeams.get(cntr).totalScore(), true);
            }
        }
    }

    public void clearAllItems() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:

                        Team.allTeams.clear();
                        setContentView(R.layout.activity_results);
                        displayTeams();
                        updateSave();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;

                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete all teams?")
                .setTitle("Clear all teams")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    public void clearTeam(int iIndexNum) {
        final int indexNum = iIndexNum;

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        Team.refresh();
                        Team.allTeams.remove(indexNum);

                        setContentView(R.layout.activity_results);
                        displayTeams();
                        updateSave();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;

                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete team " + Team.allTeams.get(indexNum).Number + " (" + Team.allTeams.get(indexNum).Name + ") ?")
                .setTitle("Clear team " + Team.allTeams.get(indexNum).Number)
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    public void goToSettings(MenuItem item) {
        Intent myIntent = new Intent(this, CustomizeActivity.class);
        this.startActivity(myIntent);
    }

    public void updateSave() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.clear();
        editor.apply();

        String compile = "";

        for (int i = 0; i < Team.allTeams.toArray().length; i++) {
            Team currTeam = Team.allTeams.get(i);
            compile = compile + "/@@Team::" + currTeam.Name + "&&" + currTeam.Number; // Start New Team
            for (int i1 = 0; i1 < Question.allQuestions.toArray().length; i1++) {
                compile = compile + "&&" + currTeam.Scores[i1];
            }
        }

        // Get Question Multipliers
        for (int i = 0; i < Question.allQuestions.toArray().length; i++) {
            Question currQuestion = Question.allQuestions.get(i);
            compile = compile + "/@@Question::" + currQuestion.multiplier;
        }

        // Get Current Color Scheme
        compile = compile + "/@@CScheme::" + selectedColor.name;
        //editor.putBoolean("FirstEver", false);
        editor.putString("saveData", compile);
        editor.putBoolean("firstEver", false);
        editor.commit();

        System.out.println("Saved String:");
        System.out.println(compile);
    }

    private void getSave() {
        SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String rawData = sharedPref.getString("saveData", "");
        Boolean firstEver = sharedPref.getBoolean("FirstEver", true);

        System.out.println("Old Saved Data:");
        System.out.println(rawData);
        String[] Objects = rawData.split("/@@");
        int qNum = 0;
        for (int i = 0; i < Objects.length; i++) {
            String[] ObjSplit = Objects[i].split("::"); // Split the Data Type from the actual data
            System.out.println("Adding in a(n) " + ObjSplit[0] + " Object");
            try {
                System.out.println("With data: " + ObjSplit[1]);

                if (ObjSplit[0].equalsIgnoreCase("Team")) {
                    String[] cData = ObjSplit[1].split("&&");
                    int[] compileQs = new int[Question.allQuestions.toArray().length];
                    for (int i1 = 0; i1 < Question.allQuestions.toArray().length; i1++) {
                        compileQs[i1] = Integer.parseInt(cData[i1 + 2]);
                    }
                    Team newTeam = new Team(cData[0], cData[1], compileQs);

                } else if (ObjSplit[0].equalsIgnoreCase("Question")) {
                    Question.allQuestions.get(qNum).multiplier = Integer.parseInt(ObjSplit[1]);
                    qNum++;
                } else if (ObjSplit[0].equalsIgnoreCase("CScheme")) {
                    ColorScheme.selectColorScheme(ObjSplit[1]);
                    selectedColor = new ColorScheme("ReadOnly", "", "", "", "", "").selectedColor;
                } else {
                    System.out.println("INVALID DATA TYPE: " + ObjSplit[0]);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Error in this object initialization!");
            }
        }

        if (firstEver) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            System.out.println("yes");
                            Intent myIntent = new Intent(getApplicationContext(), Tutorial.class);
                            MainActivity.this.startActivity(myIntent);
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            System.out.println("No");
                            break;

                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Would you like a quick tutorial on how to use the app?").show();
                    //.setTitle("Welcome")
                    //.setPositiveButton("Yes", dialogClickListener)
                    //.setNegativeButton("No", dialogClickListener).show();
        }

            editor.putBoolean("FirstEver", false);
            editor.commit();
    }
}

