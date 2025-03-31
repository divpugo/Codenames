package codingweek.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Stats implements Serializable {
    private int blueTeamWins;
    private int redTeamWins;
    private int gamesLaunched;
    private int totalCorrectGuesses;
    private int blueTeamClueSubmissions;
    private int redTeamClueSubmissions;

    private ArrayList<ClueStats> blueTeamClueStats;
    private ArrayList<ClueStats> redTeamClueStats;

    public Stats() {
        this.blueTeamWins = 0;
        this.redTeamWins = 0;
        this.gamesLaunched = 0;
        this.totalCorrectGuesses = 0;
        this.blueTeamClueSubmissions = 0;
        this.redTeamClueSubmissions = 0;
        this.blueTeamClueStats = new ArrayList<ClueStats>();
        this.redTeamClueStats = new ArrayList<ClueStats>();
    }

    public void incrementBlueTeamWins() {
        this.blueTeamWins++;
    }

    public void incrementRedTeamWins() {
        this.redTeamWins++;
    }

    public void incrementGamesLaunched() {
        this.gamesLaunched++;
    }

    public void addCorrectGuesses(int correctGuesses) {
        this.totalCorrectGuesses += correctGuesses;
    }

    public void addBlueTeamClue(int clueNb, int correctGuesses) {
        this.blueTeamClueSubmissions++;
        blueTeamClueStats.add(new ClueStats(clueNb, correctGuesses));
    }

    public void addRedTeamClue(int clueNb, int correctGuesses) {
        this.redTeamClueSubmissions++;
        redTeamClueStats.add(new ClueStats(clueNb, correctGuesses));
    }
    
    @Override
    public String toString() {
        String res = "Stats:\n" +
                "Blue Team Wins: " + blueTeamWins + "\n" +
                "Red Team Wins: " + redTeamWins + "\n" +
                "Games Launched: " + gamesLaunched + "\n" +
                "Total Correct Guesses: " + totalCorrectGuesses + "\n" +
                "Blue Team Clue Submissions: " + blueTeamClueSubmissions + "\n" +
                "Red Team Clue Submissions: " + redTeamClueSubmissions + "\n" +
                "Blue Team Clue Stats: ";
                // "Blue Team Clue Stats: " + blueTeamClueStats.values() + "\n" +
                // "Red Team Clue Stats: " + redTeamClueStats.values();
        for (ClueStats clueStats : blueTeamClueStats) {
            res += clueStats.toString() + " ; ";
        }
        res += "\nRed Team Clue Stats: ";
        for (ClueStats clueStats : redTeamClueStats) {
            res += clueStats.toString() + " ; ";
        }
        return res;
        }
    public int getBlueTeamWins() {
        return blueTeamWins;
    }

    public int getRedTeamWins() {
        return redTeamWins;
    }

    public int getGamesLaunched() {
        return gamesLaunched;
    }

    public int getTotalCorrectGuesses() {
        return totalCorrectGuesses;
    }

    public int getBlueTeamClueSubmissions() {
        return blueTeamClueSubmissions;
    }

    public int getRedTeamClueSubmissions() {
        return redTeamClueSubmissions;
    }

    public ArrayList<ClueStats> getBlueTeamClueStats() {
        return blueTeamClueStats;
    }

    public ArrayList<ClueStats> getRedTeamClueStats() {
        return redTeamClueStats;
    }
    public static class ClueStats {
        private int clueNb;
        private int correctGuesses;

        public ClueStats(int clueNb, int correctGuesses) {
            this.clueNb = clueNb;
            this.correctGuesses = correctGuesses;
        }

        public int getClueNb() {
            return clueNb;
        }

        public int getCorrectGuesses() {
            return correctGuesses;
        }

        public void setCorrectGuesses(int correctGuesses) {
            this.correctGuesses = correctGuesses;
        }

        @Override
        public String toString() {
            return "[For " + clueNb + " clue(s) given : " + correctGuesses + " correct guesses]";
        }
    }
}
