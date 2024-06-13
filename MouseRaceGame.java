package pac;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class MouseRaceGame extends Application {
    private Pane root;
    private List<GameElement> elements;
    private Text timerText;
    private long startTime;
    private boolean dialogShown = false;
    private SQLiteHelper dbHelper;

    private AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            updateGame();
        }
    };

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        root = new Pane();
        elements = new ArrayList<>();
        timerText = new Text(10, 20, "Time: 0");
        dbHelper = new SQLiteHelper();

        Button startButton = new Button("Start");
        startButton.setLayoutX(250);
        startButton.setLayoutY(200);
        startButton.setOnAction(event -> startGame());

        root.getChildren().addAll(timerText, startButton);

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("Mouse Race Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void startGame() {
        root.getChildren().clear();
        root.getChildren().add(timerText);

        startTime = System.currentTimeMillis();
        dialogShown = false;

        Random random = new Random();
        int numElements = 3 + random.nextInt(8);
        for (int i = 0; i < numElements; i++) {
            double x = Math.random() * 500;
            double y = Math.random() * 300;
            double size = 20 + Math.random() * 30;

            GameElement element;
            int rand = Integer.MAX_VALUE;
            if(i > 2) {
                rand = random.nextInt(0, 3);
            }
            if (i == 0 || rand == 0) {
                element = new Collect(x, y, size);
            } else if (i == 1 || rand == 1) {
                element = new Avoid(x, y, size);
            } else {
                element = new Change(x, y, size);
            }
            elements.add(element);
            addElement(element);
        }

        timer.start();
    }

    private void addElement(GameElement e){
        root.getChildren().add(e.getShape());
    }

    private void updateGame() {
        long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
        timerText.setText("Time: " + elapsedTime);

        if (!dialogShown) {
            checkLoss();
            checkVictory();
        }
    }

    private void checkLoss() {
        boolean anyLoss = elements.stream().anyMatch(GameElement::isLoss);
        if (anyLoss && !dialogShown) {
            dialogShown = true;
            timer.stop();
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Game Over");
                alert.setHeaderText("You Lose!");
                alert.setContentText("Better luck next time!");

                alert.showAndWait().ifPresent(response -> {
                    showTopScores();
                });
            });
        }
    }

    private void checkVictory() {
        boolean allCollected = elements.stream().filter(e -> e instanceof Collect || e instanceof Change).allMatch(e -> !e.getShape().isVisible());
        if (allCollected && !dialogShown) {
            dialogShown = true;
            long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
            timerText.setText("You Win!");
            timer.stop();

            Platform.runLater(() -> {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("You Win!");
                dialog.setHeaderText("Congratulations! You collected all items.");
                dialog.setContentText("Please enter your name:");

                Optional<String> result = dialog.showAndWait();
                result.ifPresent(name -> {
                    dbHelper.insertScore(name, elapsedTime);
                    showTopScores();
                });
            });
        }
    }

    private void showTopScores() {
        root.getChildren().clear();
        root.getChildren().add(timerText);

        Text topScoresText = new Text(10, 50, "Top Scores:");
        root.getChildren().add(topScoresText);

        List<String> topScores = dbHelper.getTopScores();
        for (int i = 0; i < topScores.size(); i++) {
            Text scoreText = new Text(10, 70 + (20 * i), topScores.get(i));
            root.getChildren().add(scoreText);
        }

        Button newGameButton = new Button("New Game");
        newGameButton.setLayoutX(250);
        newGameButton.setLayoutY(200);
        newGameButton.setOnAction(event -> resetGame());
        root.getChildren().add(newGameButton);

        dialogShown = false;
    }

    private void resetGame() {
        root.getChildren().clear();
        elements.clear();
        timerText.setText("Time: 0");

        Button startButton = new Button("Start");
        startButton.setLayoutX(250);
        startButton.setLayoutY(200);
        startButton.setOnAction(event -> startGame());

        root.getChildren().addAll(timerText, startButton);
    }
}
