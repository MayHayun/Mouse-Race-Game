package pac;

import javafx.scene.shape.Shape;

public abstract class GameElement {
    protected Shape shape;
    protected double x, y;
    protected double size;
    protected boolean isLoss = false;

    public GameElement(double x, double y, double size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }

    public Shape getShape() {
        return shape;
    }

    public void setLoss(boolean isLoss) {
        this.isLoss = isLoss;
    }

    public boolean isLoss() {
        return isLoss;
    }

    public abstract void onClicked();

}
