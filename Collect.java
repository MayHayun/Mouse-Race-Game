package pac;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

public class Collect extends GameElement {
    private TranslateTransition translateTransition;

    public Collect(double x, double y, double size) {
        super(x, y, size);
        shape = new Rectangle(size, size);
        shape.setFill(Color.GREEN);
        shape.setTranslateX(x);
        shape.setTranslateY(y);

        translateTransition = new TranslateTransition(Duration.seconds(2), shape);
        translateTransition.setByY(100);
        translateTransition.setAutoReverse(true);
        translateTransition.setCycleCount(TranslateTransition.INDEFINITE);
        translateTransition.play();

        shape.setOnMouseClicked(event -> onClicked());
    }

    @Override
    public void onClicked() {
        shape.setVisible(false);
    }
}
