package pac;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.animation.RotateTransition;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import java.util.Random;

public class Change extends GameElement {
    private RotateTransition rotateTransition;
    private boolean isCollect;

    private PauseTransition pauseTransition;

    public Change(double x, double y, double size) {
        super(x, y, size);
        shape = new Rectangle(size, size);
        shape.setLayoutX(x);
        shape.setLayoutY(y);

        rotateTransition = new RotateTransition(Duration.seconds(1), shape);
        rotateTransition.setByAngle(360);
        rotateTransition.setCycleCount(RotateTransition.INDEFINITE);
        rotateTransition.play();

        switchBehavior();
    }

    private void switchBehavior() {
        Random random = new Random();
        isCollect = random.nextBoolean();
        if (isCollect) {
            shape.setFill(Color.GREEN);
        } else {
            shape.setFill(Color.RED);
        }

        shape.setOnMouseClicked(event -> onClicked());

        pauseTransition = new PauseTransition(Duration.seconds(2));
        pauseTransition.setOnFinished(event -> switchBehavior());
        pauseTransition.play();
    }

    @Override
    public void onClicked() {
        if (isCollect) {
            shape.setVisible(false);
        } else {
            this.setLoss(true);
        }
    }
}
