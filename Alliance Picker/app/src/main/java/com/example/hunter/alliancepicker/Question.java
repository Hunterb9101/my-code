package com.example.hunter.alliancepicker;

import java.util.ArrayList;

/**
 * Created by Hunter on 8/25/2015.
 */
public class Question {
    static int gQuestionNo = 1;
    static ArrayList<Question> allQuestions = new ArrayList<>();
    String question = "";
    int multiplier = 1;
    int questionNo = 0;
    String inputType = "";

    public Question(String iQuestion, int iMultiplier, String inpType) {
        multiplier = iMultiplier;
        questionNo = gQuestionNo;
        question = iQuestion;
        inputType = inpType;
        gQuestionNo++;
        if (!iQuestion.equalsIgnoreCase("readOnly")) {
            allQuestions.add(this);
        }
    }
}
