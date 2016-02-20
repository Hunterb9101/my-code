package com.example.hunter.alliancepicker;

import java.util.ArrayList;

/**
 * Created by Hunter on 8/24/2015.
 */
public class Team {
    public static Utils util = new Utils();
    public static ArrayList<Team> allTeams = new ArrayList<>();
    String Name = "";
    String Number = "";
    int[] Scores = new int[9];

    public Team(String iName, String iNumber, int[] iScores){
        Name = iName;
        Number = iNumber;
        Scores = iScores;
        if(!Name.equalsIgnoreCase("ReadOnly")){
            allTeams.add(this);
        }
    }

    public int totalScore() {
        int sum = 0;
        for (int i : Scores)
            sum += i;
        return sum;
    }

    public static ArrayList<Team> sortTeams(ArrayList<Team> Teams){
        Team[] unSortedTeams = util.Obj2Team(Teams.toArray()); //Comes in as Object[], Obj2Int makes it an integer.
        ArrayList<Team> sortedTeams = new ArrayList<Team>();
        int[] Scores = util.ExtractTeamScores(unSortedTeams);


        for(int cntr = 0; cntr < unSortedTeams.length; cntr++){ //Make sure all teams are put back into the array
            int highestScore = 0;
            int highScoreIndex = 0;
            for(int cntr2 = 0; cntr2 < unSortedTeams.length; cntr2++){ //Compare Scores
                if(Scores[cntr2] > highestScore){
                    highestScore = Scores[cntr2];
                    highScoreIndex = cntr2;
                }
            }
            sortedTeams.add(unSortedTeams[highScoreIndex]);
            Scores[highScoreIndex] = -1; //Assures that the team won't be picked again
        }

        return sortedTeams;
    }

    public static void refresh(){
        ArrayList<Team> SortedTeams = Team.sortTeams(Team.allTeams);
        Team.allTeams = SortedTeams;
    }

    public int findByName(String name){
        for(int cntr = 0;cntr<this.allTeams.toArray().length; cntr++){
            if(name.equalsIgnoreCase(this.allTeams.get(cntr).Name)){
                return cntr;
            }
        }
        return -1;
    }
}
