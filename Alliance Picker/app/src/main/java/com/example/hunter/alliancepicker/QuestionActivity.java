package com.example.hunter.alliancepicker;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class QuestionActivity extends AppCompatActivity {
    Question questionClass = new Question("ReadOnly",0,"");
    Team teamClass = new Team("ReadOnly","",new int[0]);

    int qRowNum = 0;

    String compileSave = "";
    ColorScheme selectedColor = new ColorScheme("ReadOnly","","","","","").selectedColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getRequestedOrientation();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        Intent intent = getIntent();
        final int teamArrayNum = Integer.parseInt(intent.getStringExtra("loadType"));
        final int runType = Integer.parseInt(intent.getStringExtra("runType"));

        if(runType == 1){
            this.setTitle("Change Preferences");
        }

        setContentView(R.layout.activity_question);

        for (int cntr = 0; cntr < questionClass.allQuestions.toArray().length; cntr++) {
            if (teamArrayNum == -1 && runType != 1) {
                if(!(runType == 1 && (questionClass.allQuestions.get(cntr).inputType.equalsIgnoreCase("Number") || questionClass.allQuestions.get(cntr).inputType.equalsIgnoreCase("Text")))){
                    Questions_CreateRow(questionClass.allQuestions.get(cntr).question, questionClass.allQuestions.get(cntr).inputType, 0.0f, "");
                }
                else {
                    qRowNum++;
                }
            }

            else if(teamArrayNum == -1 && runType == 1){
                float ratingBarValue = (float) questionClass.allQuestions.get(cntr).multiplier / 2;

                if(!(runType == 1 && (questionClass.allQuestions.get(cntr).inputType.equalsIgnoreCase("Number") || questionClass.allQuestions.get(cntr).inputType.equalsIgnoreCase("Text")))){
                    Questions_CreateRow(questionClass.allQuestions.get(cntr).question, questionClass.allQuestions.get(cntr).inputType, ratingBarValue, "");
                }
                else {
                    qRowNum++;
                }
            }

            else {
                Team teamClass = new Team("ReadOnly", "0000", new int[0]);
                float ratingBarValue = (float) teamClass.allTeams.get(teamArrayNum).Scores[cntr] / questionClass.allQuestions.get(cntr).multiplier / 2;
                String editTextValue = "";

                if (questionClass.allQuestions.get(cntr).inputType.equalsIgnoreCase("Number")) {
                    editTextValue = teamClass.allTeams.get(teamArrayNum).Number;
                }

                if (questionClass.allQuestions.get(cntr).inputType.equalsIgnoreCase("Text")) {
                    editTextValue = teamClass.allTeams.get(teamArrayNum).Name;
                }
                Questions_CreateRow(questionClass.allQuestions.get(cntr).question, questionClass.allQuestions.get(cntr).inputType, ratingBarValue, editTextValue);
            }
        }

        Questions_CreateMenu();

        final EditText teamName = ((EditText) findViewById(1100));
        final EditText teamNumber = ((EditText) findViewById(1101));


        Button button9 = (Button) findViewById(R.id.submit);
        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qRowNum = 0;
                int[] CompileQs = new int[questionClass.allQuestions.toArray().length];
                String scoreToString = "";

                for (int cntr = 0; cntr < questionClass.allQuestions.toArray().length; cntr++) {
                    if (questionClass.allQuestions.get(cntr).inputType.equalsIgnoreCase("RatingBar")) {
                        RatingBar rb = (RatingBar) findViewById(1100 + cntr);

                        scoreToString = "" + CompileQs[cntr - 1];
                        compileSave = compileSave + "%" + scoreToString;

                        try {
                            if(runType == 0){
                                CompileQs[cntr] = ((int) (rb.getRating() * 2)) * questionClass.allQuestions.get(cntr).multiplier;
                            }
                            else{
                                CompileQs[cntr] = questionClass.allQuestions.get(cntr).multiplier;
                                questionClass.allQuestions.get(cntr).multiplier = ((int) (rb.getRating() * 2));
                            }
                        } catch (NullPointerException e) {
                            CompileQs[cntr] = 0;
                        }
                    }
                }

                if (runType == 0) {
                    compileSave = compileSave + "@" + teamName.getText().toString() + "%" + teamNumber.getText().toString();

                    Team newTeam = new Team(teamName.getText().toString(), teamNumber.getText().toString(), CompileQs);

                    if (teamArrayNum != -1) {
                        newTeam.allTeams.remove(teamArrayNum);
                    }

                    ArrayList<Team> SortedTeams = newTeam.sortTeams(newTeam.allTeams);
                    newTeam.allTeams = SortedTeams;
                }

                else{
                    for(int cntr=0;cntr< teamClass.allTeams.toArray().length; cntr++){
                        for(int cntr2=0; cntr2 < questionClass.allQuestions.toArray().length; cntr2++){
                            try {
                                teamClass.allTeams.get(cntr).Scores[cntr2] = teamClass.allTeams.get(cntr).Scores[cntr2] / CompileQs[cntr2] * questionClass.allQuestions.get(cntr2).multiplier;
                            }
                            catch(ArithmeticException e){
                                teamClass.allTeams.get(cntr).Scores[cntr2] = 0;
                            }
                        }
                    }
                }

                //questionClass.allQuestions.clear();

                Intent myIntent = new Intent(QuestionActivity.this, MainActivity.class);
                QuestionActivity.this.startActivity(myIntent);
                QuestionActivity.this.finish();
            }
        });


        Button back = (Button) findViewById(R.id.BackButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qRowNum = 0;

                Intent myIntent = new Intent(QuestionActivity.this, MainActivity.class);
                QuestionActivity.this.startActivity(myIntent);
                QuestionActivity.this.finish();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_question, menu);
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

    public void Questions_CreateRow(String questionText, String questionType, Float rbValue, String etValue) {
        TableLayout tl = (TableLayout) findViewById(R.id.QuestionsTable);

        ScrollView sv = (ScrollView) findViewById(R.id.top_questions);
        sv.setBackgroundColor(Color.parseColor(selectedColor.EdgeBgColor));

        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));


        TextView question = (TextView) getLayoutInflater().inflate(R.layout.results_row, null);
        question.setId(1000 + qRowNum);
        question.setTextColor(Color.parseColor(selectedColor.TxtColor));
        question.setText(questionText);

        RatingBar rb = (RatingBar) getLayoutInflater().inflate(R.layout.question_ratingbar, null);
        EditText et = (EditText) getLayoutInflater().inflate(R.layout.question_textedit, null);

        et.setTextColor(Color.parseColor(ColorScheme.selectedColor.TxtColor));

        tr.addView(question);
        if (questionType.equalsIgnoreCase("RatingBar")) {
            rb.setRating(rbValue);
            rb.setId(1100 + qRowNum);
            tr.addView(rb);
        }
        else if (questionType.equalsIgnoreCase("Text")) {
            et.setId(1100 + qRowNum);
            et.setText(etValue);
            et.setInputType(InputType.TYPE_CLASS_TEXT);
            tr.addView(et);
        }
        else if (questionType.equalsIgnoreCase("Number")) {
            et.setId(1100 + qRowNum);
            et.setText(etValue);
            et.setInputType(InputType.TYPE_CLASS_NUMBER);
            tr.addView(et);
        }
        else {
            System.out.println("ERROR: TYPE ISN'T KNOWN!");
        }

        if (qRowNum % 2 == 0) {
            tr.setBackgroundColor(Color.parseColor(selectedColor.BgColor2));
        }
        else {
            tr.setBackgroundColor(Color.parseColor(selectedColor.BgColor));
        }

        tl.addView(tr);
        qRowNum++;
    }

    public void Questions_CreateMenu() {
        TableLayout tl = (TableLayout) findViewById(R.id.QuestionsTable);

        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        Button back = (Button) getLayoutInflater().inflate(R.layout.question_back_button, null);
        Button submit = (Button) getLayoutInflater().inflate(R.layout.question_submit_button, null);
        tr.addView(back);
        tr.addView(submit);
        tl.addView(tr);
        tr.setBackgroundColor(Color.parseColor(selectedColor.BgColor));
    }
}
