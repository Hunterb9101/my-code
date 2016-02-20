package com.example.hunter.alliancepicker;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class InfoActivity extends AppCompatActivity {
    ColorScheme selectedColor = new ColorScheme("ReadOnly","","","","","").selectedColor;
    Team teamClass = new Team("ReadOnly","",new int[0]);
    Question questionClass = new Question("ReadOnly",0,"");
    int teamArrayNum = -1;
    int rowNum = 0;
    int expandableState = 0;
    double scoreThresh = .25;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Intent intent = getIntent();
        teamArrayNum = Integer.parseInt(intent.getStringExtra("loadType"));
        Info_CreateMain();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void Info_CreateMain(){
        int numStrengths = 0;
        int numWeaknesses = 0;

        Info_CreateRow("Team " + teamClass.allTeams.get(teamArrayNum).Number + "'s Strengths", true);

        // Find Strengths
        for(int cntr = 0;cntr< questionClass.allQuestions.toArray().length;cntr++){
            int avg = findAverageScore(cntr);
            if((int)(avg*(1+scoreThresh)) < teamClass.allTeams.get(teamArrayNum).Scores[cntr]){
                Info_CreateRow("    - " + questionClass.allQuestions.get(cntr).question,false);
                numStrengths++;
            }
        }

        if(numStrengths == 0 && teamClass.allTeams.toArray().length < 2){
            Info_CreateRow("There aren't enough teams to find a strength.",false);
        }
        else if(numStrengths == 0){
            Info_CreateRow("This team has no strengths",false);
        }

        Info_CreateRow("Team " + teamClass.allTeams.get(teamArrayNum).Number + "'s Weaknesses", true);

        for(int cntr = 2;cntr<questionClass.allQuestions.toArray().length;cntr++){
            int avg = findAverageScore(cntr);
            if((int)(avg*(1-scoreThresh)) > teamClass.allTeams.get(teamArrayNum).Scores[cntr]){
                Info_CreateRow("    - " + questionClass.allQuestions.get(cntr).question,false);
                numWeaknesses++;
            }
        }

        if(numWeaknesses == 0 && teamClass.allTeams.toArray().length < 2){
            Info_CreateRow("There aren't enough teams to find a weakness.", false);
        }
        else if(numWeaknesses == 0){
            Info_CreateRow("This team has no weaknesses",false);
        }

        Info_CreateMenu();

        Button back = (Button) findViewById(R.id.BackButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rowNum = 0;

                Intent myIntent = new Intent(InfoActivity.this, MainActivity.class);
                InfoActivity.this.startActivity(myIntent);
                InfoActivity.this.finish();

            }
        });

    }
    public void Info_CreateRow(String dispText, Boolean header) {
        TableLayout tl = (TableLayout)findViewById(R.id.infoTable);

        ScrollView sv = (ScrollView)findViewById(R.id.top_info);
        sv.setBackgroundColor(Color.parseColor(selectedColor.EdgeBgColor));

        TableRow tr1 = new TableRow(this);

        tr1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));


        TextView textview;

        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.span = 3;
        tr1.setLayoutParams(params);

        if(header){ textview = (TextView) getLayoutInflater().inflate(R.layout.info_header, null); }
        else{ textview = (TextView) getLayoutInflater().inflate(R.layout.info_row,null);}

        /* Team Text */
        textview.setId(5300 + rowNum);
        textview.setTextColor(Color.parseColor(selectedColor.TxtColor));
        textview.setText(dispText);

        if (rowNum % 2 == 0) {tr1.setBackgroundColor(Color.parseColor(selectedColor.BgColor));}
        else {tr1.setBackgroundColor(Color.parseColor(selectedColor.BgColor2));}

        tr1.addView(textview);

        tl.addView(tr1);
        rowNum++;
    }

    public void Info_CreateMenu() {
        TableLayout tl = (TableLayout) findViewById(R.id.infoTable);

        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        Button back = (Button) getLayoutInflater().inflate(R.layout.question_back_button, null);

        final Button submit = (Button) getLayoutInflater().inflate(R.layout.info_more, null);
        submit.setLayoutParams(new TableRow.LayoutParams(2));

        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (expandableState == 0) {
                    Info_CreateSpinnerTable();
                    expandableState = 1;
                    submit.setText("See Less");
                } else {
                    finish();
                    startActivity(getIntent());
                    expandableState = 0;
                    submit.setText("See More");
                }
            }
        });
        tr.addView(back);
        tr.addView(submit);
        tl.addView(tr);
        tr.setBackgroundColor(Color.parseColor(selectedColor.BgColor));
    }

    public void Info_CreateSpinnerTable(){
        TableLayout tl = (TableLayout) findViewById(R.id.spinnerTable);

        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        Spinner teamSelect = (Spinner) getLayoutInflater().inflate(R.layout.info_spinner, null);//Main Spinner
        TextView tv = (TextView) getLayoutInflater().inflate(R.layout.info_header,null);
        tv.setText("Compare To:");

        List<String> items = new ArrayList<String>();

        items.add("Average");
        for(int cntr=0; cntr<teamClass.allTeams.toArray().length; cntr++){
            if(teamArrayNum != cntr){ //Make sure not to add the team that is being compared!!!
                items.add(teamClass.allTeams.get(cntr).Name);
            }
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.info_spinner_text, items);

        dataAdapter.setDropDownViewResource(R.layout.info_spinner_item);

        teamSelect.setAdapter(dataAdapter);

        teamSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                ((TableLayout) findViewById(R.id.scoreTable)).removeAllViews();
                Info_CreateScoreTable(parent.getItemAtPosition(pos).toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        tr.addView(tv);
        tr.addView(teamSelect);
        tl.addView(tr);
    }

    public void Info_CreateScoreTable(String teamComparedName){
        Team team1 = teamClass.allTeams.get(teamArrayNum);
        Team team2;
        Info_CreateScoreTableRow(team1.Name, teamComparedName, "Teams: ", true); // Shows team names

        if(teamComparedName.equalsIgnoreCase("Average")){
            int[] scores = new int[questionClass.allQuestions.toArray().length];

            for(int cntr=0; cntr<(questionClass.allQuestions.toArray().length) ;cntr++){
                scores[cntr] = findAverageScore(cntr);
            }

            team2 = new Team("ReadOnly","0000",scores);
        }
        else{
            team2 = teamClass.allTeams.get(teamClass.findByName(teamComparedName));
        }

        for(int cntr=2;cntr<questionClass.allQuestions.toArray().length;cntr++){
            Info_CreateScoreTableRow(Integer.toString(team1.Scores[cntr]),Integer.toString(team2.Scores[cntr]), questionClass.allQuestions.get(cntr).question,false);
        }

        Info_CreateScoreTableRow(Integer.toString(team1.totalScore()), Integer.toString(team2.totalScore()), "Total Score", false);

    }

    public void Info_CreateScoreTableRow(String c1, String c2, String title, Boolean header) {
        TableLayout tl = (TableLayout) findViewById(R.id.scoreTable);

        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView rowTitle;
        if (header) { rowTitle = (TextView) getLayoutInflater().inflate(R.layout.info_header_2, null);}
        else { rowTitle = (TextView) getLayoutInflater().inflate(R.layout.info_row, null);}

        rowTitle.setText(title);

        TextView column1;
        if (header) { column1 = (TextView) getLayoutInflater().inflate(R.layout.info_header_2, null);}
        else { column1 = (TextView) getLayoutInflater().inflate(R.layout.info_row, null);}

        column1.setText(c1);

        TextView column2;
        if (header) { column2 = (TextView) getLayoutInflater().inflate(R.layout.info_header_2, null);}
        else { column2 = (TextView) getLayoutInflater().inflate(R.layout.info_row, null);}
        column2.setText(c2);

        if (rowNum % 2 == 0) {tr.setBackgroundColor(Color.parseColor(selectedColor.BgColor));}
        else {tr.setBackgroundColor(Color.parseColor(selectedColor.BgColor2));}

        rowTitle.setTextColor(Color.parseColor(ColorScheme.selectedColor.TxtColor));
        column1.setTextColor(Color.parseColor(ColorScheme.selectedColor.TxtColor));
        column2.setTextColor(Color.parseColor(ColorScheme.selectedColor.TxtColor));

        tr.addView(rowTitle);
        tr.addView(column1);
        tr.addView(column2);
        tl.addView(tr);

        rowNum++;
    }

    public int findAverageScore(int qIdx){
        int length = teamClass.allTeams.toArray().length;
        int sumTeamScores = 0;

        for(int cntr = 0; cntr < teamClass.allTeams.toArray().length; cntr++){ // Adds in all team scores for that question
            sumTeamScores = sumTeamScores + teamClass.allTeams.get(cntr).Scores[qIdx];
        }


        return sumTeamScores/length;
    }
}
