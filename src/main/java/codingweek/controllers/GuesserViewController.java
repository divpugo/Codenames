package codingweek.controllers;

import codingweek.models.Game;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.scene.layout.BorderPane;

public class GuesserViewController implements codingweek.Observer {
    @FXML
    Button btnTurn;

    @FXML
    private GuesserBoardController guesserBoardController;

    @FXML
    private ImageView sandTimerView;

    @FXML
    private Label timeLabel;

    @FXML
    private BorderPane rootPane;

    private Game game;

    private static final int FRAME_COUNT = 10; // Nombre de frames dans le sprite
    private Image[] blueFrames;
    private Image[] redFrames;
    private int currentFrame = 0;
    private int timeRemaining;
    private Timeline timeline;
    private boolean timelineRunning = false;

    public void initialize() {
        game = Game.getInstance();
        game.ajouterObservateur(this);

        loadFrames();
        sandTimerView.setPreserveRatio(true); 
        sandTimerView.setVisible(false);
        timeLabel.setVisible(false);

        btnTurn.setOnAction(e -> {
            turn();
        });
        btnTurn.setDisable(true);
        updateBackground();
    }

    public void reagir() {
        if (game.getNbCardsReturned() < 1) {
            btnTurn.setDisable(true);
        } else {
            btnTurn.setDisable(false);
        }
        if (game.isTimerRunning()) {
            startAnimationAndCountdown();
        } else {
            stopTimeLine();
        }
        updateBackground();
    }

    private void loadFrames() {
        blueFrames = new Image[FRAME_COUNT];
        for (int i = 0; i < FRAME_COUNT; i++) {
            blueFrames[i] = new Image(getClass().getResourceAsStream("/Images/hourglass_blue/frame_" + String.format("%02d", i+1) + ".png"));
        }
        sandTimerView.setImage(blueFrames[0]);

        redFrames = new Image[FRAME_COUNT];
        for (int i = 0; i < FRAME_COUNT; i++) {
            redFrames[i] = new Image(getClass().getResourceAsStream("/Images/hourglass_red/frame_" + String.format("%02d", i+1) + ".png"));
        }
        sandTimerView.setImage(redFrames[0]);
    }

    private void startAnimationAndCountdown() {
        // Réinitialisation
        if (!timelineRunning) { // Sinon ça se lance 3 fois à cause des réactions
            timelineRunning = true;
            currentFrame = 0;
            timeRemaining = game.getTimeLimit();  // Remets le temps restant à la durée du jeu
            timeLabel.setText("Remaining time : " + timeRemaining);
            timeLabel.setVisible(true);

            if (game.isBlueTurn()) {
                sandTimerView.setImage(blueFrames[currentFrame]);
                sandTimerView.setVisible(true);
            } else {
                sandTimerView.setImage(redFrames[currentFrame]);
                sandTimerView.setVisible(true);
            }

            timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    action();
                })
            );
            timeline.setCycleCount(game.getTimeLimit());
            timeline.play();
        }
    }
    
    private void action() {
        currentFrame = (currentFrame + 1) % 10;
        if (game.isBlueTurn()) {
            sandTimerView.setImage(blueFrames[currentFrame]);
            sandTimerView.setVisible(true);
        } else {
            sandTimerView.setImage(redFrames[currentFrame]);
            sandTimerView.setVisible(true);
        }
        timeRemaining--;
        timeLabel.setText("Remaining time : " + timeRemaining);
        timeLabel.setVisible(true);
        if (timeRemaining <= 0) {
            stopTimeLine();
            turn();
        }
    }

    private void stopTimeLine() {
        if (timeline != null) {
            timeLabel.setVisible(false);
            sandTimerView.setVisible(false);
            timeline.stop();
            timelineRunning = false;
        }
    }

    private void turn() {
        if (!game.isSpyTurn()) {
            game.changeTurn();
        }
    }

    private void updateBackground() {
        Boolean currentTeam = game.isBlueTurn();
        String backgroundColor;

        if (currentTeam) {
            backgroundColor = "#85C4FF";
        } else {
            backgroundColor = "#F6A2A7";
        }

        rootPane.setStyle("-fx-background-color: " + backgroundColor + ";");
    }

}
