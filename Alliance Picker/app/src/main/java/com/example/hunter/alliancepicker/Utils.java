package com.example.elliott.alliancepicker;

/**
 * Created by Hunter on 8/25/2015.
 */
public class Utils{
    public Team[] Obj2Team(Object[] inputData){
        Team[] outData = new Team[inputData.length];

        for(int cntr = 0; cntr < outData.length; cntr++){
            outData[cntr] = (Team) inputData[cntr];
        }
        return outData;
    }

    public int[] ExtractTeamScores(Team[] teamData){
        int[] Scores = new int[teamData.length];
        for(int cntr = 0; cntr < teamData.length; cntr++){
            Scores[cntr] = teamData[cntr].totalScore();
        }
        return Scores;
    }
}
