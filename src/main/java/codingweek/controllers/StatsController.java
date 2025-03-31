package codingweek.controllers;

import codingweek.models.Game;
import codingweek.models.PageManager;
import codingweek.models.Stats;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import java.util.Arrays;

public class StatsController {

    @FXML
    private Label gamesLaunchedLabel;

    @FXML
    private Label blueTeamWinsLabel;

    @FXML
    private Label redTeamWinsLabel;

    @FXML
    private Label blueTeamClueSubmissionsLabel;

    @FXML
    private Label redTeamClueSubmissionsLabel;

    @FXML
    private ListView<String> blueTeamClueStatsList;

    @FXML
    private ListView<String> redTeamClueStatsList;

    @FXML
    private ComboBox<String> dataSelector;

    @FXML
    private PieChart pieChart;

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private CategoryAxis categoryAxis;

    @FXML
    private NumberAxis numberAxis;

    @FXML
    private Button resumeButton;

    private Stats stats;
    private PageManager pageManager;
    private Game game;

    public void initialize() {
        game = Game.getInstance();
        pageManager = PageManager.getInstance();
        if (game.isGameOver()) {
            resumeButton.setText("Exit to menu");
        } else {
            resumeButton.setText("Resume");
        }
        resumeButton.setOnAction(e -> {
            resumeButtonAction();
        });
    }

    private void resumeButtonAction() {
        if (game.isGameOver()) {
            pageManager.loadMenuWindowView();
            pageManager.closeSpyView();
        } else {
            pageManager.loadGuesserView();
            pageManager.loadSpyView();
        }
    }

    public void setStats(Stats stats) {
        this.stats = stats;
        updateStatsDisplay();
        setupDataSelector();
    }

    private void updateStatsDisplay() {
        if (stats == null) {
            System.err.println("Stats object is null. Unable to update display.");
            return;
        }

        // Update general stats labels
        gamesLaunchedLabel.setText("Games Launched: " + stats.getGamesLaunched());
        blueTeamWinsLabel.setText("Blue Team Wins: " + stats.getBlueTeamWins());
        redTeamWinsLabel.setText("Red Team Wins: " + stats.getRedTeamWins());
        blueTeamClueSubmissionsLabel.setText("Blue Team Clues Given: " + stats.getBlueTeamClueSubmissions());
        redTeamClueSubmissionsLabel.setText("Red Team Clues Given: " + stats.getRedTeamClueSubmissions());

        // Populate Blue Team clue details
        blueTeamClueStatsList.getItems().clear();
        stats.getBlueTeamClueStats().forEach(clue -> {
            blueTeamClueStatsList.getItems().add("For " + clue.getClueNb() + " clue(s) given : " + clue.getCorrectGuesses() + " correct guesses");
        });

        // Populate Red Team clue details
        redTeamClueStatsList.getItems().clear();
        stats.getRedTeamClueStats().forEach(clue -> {
            redTeamClueStatsList.getItems().add("For " + clue.getClueNb() + " clue(s) given : " + clue.getCorrectGuesses() + " correct guesses");
        });
    }

    private void setupDataSelector() {
        dataSelector.setItems(FXCollections.observableArrayList(
            "Wins vs Games Launched (PieChart)",
            "Clues Given by Team (BarChart)",
            "Blue Team Clues (StackedBarChart)",
            "Red Team Clues (StackedBarChart)"
        ));
        // Définir la sélection par défaut
        dataSelector.setValue("Wins vs Games Launched (PieChart)");
        displayWinsVsGamesPieChart();
        dataSelector.valueProperty().addListener((observable, oldValue, newValue) -> updateChart(newValue));
    }
    
    private void updateChart(String selectedData) {
        pieChart.setVisible(false);
        barChart.setVisible(false);
    
        switch (selectedData) {
            case "Wins vs Games Launched (PieChart)":
                displayWinsVsGamesPieChart();
                break;
            case "Clues Given by Team (BarChart)":
                displayCluesGivenBarChart();
                break;
            case "Blue Team Clues (StackedBarChart)":
                displayBlueTeamStackedBarChart();
                break;
            case "Red Team Clues (StackedBarChart)":
                displayRedTeamStackedBarChart();
                break;
            default:
                System.err.println("Unknown data selection: " + selectedData);
        }
    }
    
    private void displayWinsVsGamesPieChart() {
        pieChart.getData().clear();
        pieChart.getData().add(new PieChart.Data("Blue Team Wins", stats.getBlueTeamWins()));
        pieChart.getData().add(new PieChart.Data("Red Team Wins", stats.getRedTeamWins()));
        pieChart.getData().add(new PieChart.Data("Games Not Won", stats.getGamesLaunched() - stats.getBlueTeamWins() - stats.getRedTeamWins()));
        pieChart.setVisible(true);
    }
    
    private void displayCluesGivenBarChart() {
        barChart.getData().clear();
        var series = new BarChart.Series<String, Number>();
        series.setName("Clues Given by Team");
        series.getData().add(new BarChart.Data<>("Blue Team", stats.getBlueTeamClueSubmissions()));
        series.getData().add(new BarChart.Data<>("Red Team", stats.getRedTeamClueSubmissions()));
        barChart.setData(FXCollections.observableArrayList(Arrays.asList(series)));
        barChart.setVisible(true);
    }
    
    private void displayBlueTeamStackedBarChart() {
        barChart.getData().clear();
    
        var seriesClues = new BarChart.Series<String, Number>();
        var seriesCorrectGuesses = new BarChart.Series<String, Number>();
        seriesClues.setName("Clue Numbers");
        seriesCorrectGuesses.setName("Correct Guesses");
    
        int index = 1; // To ensure uniqueness for X-axis categories
        for (var clue : stats.getBlueTeamClueStats()) {
            String category = "Clue Instance " + index++;
            seriesClues.getData().add(new BarChart.Data<>(category, clue.getClueNb()));
            seriesCorrectGuesses.getData().add(new BarChart.Data<>(category, clue.getCorrectGuesses()));
        }
    
        barChart.getData().addAll(Arrays.asList(seriesClues, seriesCorrectGuesses));
        barChart.setVisible(true);
    }
    
    
    private void displayRedTeamStackedBarChart() {
        barChart.getData().clear();
    
        var seriesClues = new BarChart.Series<String, Number>();
        var seriesCorrectGuesses = new BarChart.Series<String, Number>();
        seriesClues.setName("Clue Numbers");
        seriesCorrectGuesses.setName("Correct Guesses");
    
        int index = 1; // To ensure uniqueness for X-axis categories
        for (var clue : stats.getRedTeamClueStats()) {
            String category = "Clue Instance " + index++;
            seriesClues.getData().add(new BarChart.Data<>(category, clue.getClueNb()));
            seriesCorrectGuesses.getData().add(new BarChart.Data<>(category, clue.getCorrectGuesses()));
        }
    
        barChart.getData().addAll(Arrays.asList(seriesClues, seriesCorrectGuesses));
        barChart.setVisible(true);
    }
    
    
}
