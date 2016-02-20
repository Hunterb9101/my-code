package com.example.hunter.alliancepicker;

import android.app.Application;

public class InstantizedVars extends Application {
    public static Boolean firstLoad = true;

    private Question q1 = new Question("Team Name",0 ,"Text"); //Text
    private Question q2 = new Question("Team Number",0, "Number"); //Number
    private Question q3 = new Question("Overall Autonomous Quality",5, "RatingBar");
    private Question q4 = new Question("Quality of Pressing Rescue Beacon",5, "RatingBar");
    private Question q5 = new Question("Quality of Parking in Mountain",5, "RatingBar");
    private Question q6 = new Question("Quality Parking in Beacon Repair Zone",5, "RatingBar");
    private Question q7 = new Question("Quality of Climbers in Bin",5, "RatingBar");
    private Question q8 = new Question("Overall Teleop Quality",5 ,"RatingBar");
    private Question q9 = new Question("Quality of Park on Mountain",5 ,"RatingBar");
    private Question q10 = new Question("Quality of Claiming an All Clear Signal",5 ,"RatingBar");
    private Question q11 = new Question("Quality of Hang",5 ,"RatingBar");
}
