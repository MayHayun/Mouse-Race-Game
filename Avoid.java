package pac;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

public class Avoid extends GameElement {
    private TranslateTransition translateTransition;

    public Avoid(double x, double y, double size) {
        super(x, y, size);
        shape = new Circle(size / 2);
        shape.setFill(Color.RED);
        shape.setLayoutX(x);
        shape.setLayoutY(y);

        // Make sure the TranslateTransition works as expected
        translateTransition = new TranslateTransition(Duration.seconds(3), shape);
        translateTransition.setByX(100);
        translateTransition.setAutoReverse(true);
        translateTransition.setCycleCount(TranslateTransition.INDEFINITE);
        translateTransition.play();

        // Add mouse click handler to the shape
        shape.setOnMouseClicked(event -> onClicked());
    }

    @Override
    public void onClicked() {
        this.setLoss(true);
        shape.setVisible(false); // Hide the shape on click
        translateTransition.stop(); // Stop the transition on click
    }
}
